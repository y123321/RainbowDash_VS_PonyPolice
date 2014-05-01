    package il.co.ovalley.rdvsponeypolice.Controller;

    /**
     * Created by yuval on 16/04/2014.
     */
    public class GameRunnable  {
/*


        public int getClockCounter() {
            return m_ClockCounter;
        }

        private volatile int m_ClockCounter;
        private int m_ClockWait;
        private int m_SpawnTime;
        private ArrayList<Cop> m_cops;
        private ArrayList<Drop> m_drops;
        private ArrayList<Shot> m_shots;

        public ArrayList<Cop> getDeadCops() {
            return m_deadCops;
        }

        private ArrayList<Cop> m_deadCops;
        private Object m_PauseObject;
        private boolean m_Pause;
        private GameLayout m_layout;
        private volatile boolean m_IsGameRunning;
        private int m_copsLimit;
        private int m_hasteCopSpawnInterval;
        private int m_copTypeLimit;
        private int m_copTypeRatio;
        private int m_minSpawnTime;
        public boolean remove(Object object) {
            m_copsCounter--;
            return m_cops.remove(object);
        }

        private int m_copsCounter;

        public GameLayout getGameLayout() {
            return m_layout;
        }

        private int m_totalCopsCounter;
        private volatile RainbowDash m_rainbowDash;

        public RainbowDash getRainbowDash() {
            return m_rainbowDash;
        }

        public GameRunnable(GameLayout layout){
            m_layout=layout;

            init();
        }
        private void init(){
            m_cops = new ArrayList<Cop>();
            m_drops=new ArrayList<Drop>();
            m_shots=new ArrayList<Shot>();
            m_ClockCounter=0;
            m_ClockWait=10;
            m_IsGameRunning=false;
            m_Pause = false;
            m_PauseObject=new Object();
            m_copsCounter=0;
            m_totalCopsCounter=0;
            m_rainbowDash=new RainbowDash(m_layout);
            m_copsLimit=40;
            m_hasteCopSpawnInterval=200;
            m_copTypeLimit=20;
            m_copTypeRatio=2;
            m_minSpawnTime=5;
            startRDListener();
        }



        @Override
        public void run() {
            m_IsGameRunning=true;
            while (m_IsGameRunning){
                try {
                    if(m_Pause) synchronized (m_PauseObject){
                        m_PauseObject.wait();
                    }
                    m_rainbowDash.runUpdate();
                    updateCopsAndDrops();
                    updateDrops();
                    updateShots();
                    spawnCops();
                    Thread.sleep(m_ClockWait);
                    m_ClockCounter++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                stopIfGameLost();
            }
        }

        private void updateDrops() {
            for(Drop drop:m_drops)
                if(drop!=null)
                    if(getClockCounter() % drop.getWaitTime() == 0) drop.runUpdate();
        }

        private void stopIfGameLost() {
            if(getRainbowDash().isLost()) m_IsGameRunning=false;
        }

        private void spawnCops() {
            if(m_ClockCounter%m_SpawnTime==0 && m_copsCounter<m_copsLimit){
                if(m_totalCopsCounter<m_copTypeLimit)spawnCop(CopType.SIMPLE);
                else{
                    if(m_totalCopsCounter%m_copTypeRatio==0) spawnCop(CopType.NINJA);
                    else spawnCop(CopType.SIMPLE);
                }
            }
            if(getClockCounter()%m_hasteCopSpawnInterval==0 &&m_SpawnTime>m_minSpawnTime)m_SpawnTime--;
        }

        private void updateShots() {
            new Thread(new ShotsRunnable(this,getShots())).start();
        }

        private void updateCopsAndDrops() {
            new Thread(new CopsAndDropsRunnable(this,getCops(),getDrops())).start();
        }
        private void spawnCop(final CopType type) {
                m_totalCopsCounter++;
                m_copsCounter++;
                final RelativeLayout.LayoutParams params = Common.getStickToBottomParams();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Cop cop;
                        switch (type) {
                            case NINJA:
                                cop = new NinjaCop(getGameLayout(), Common.getStickToBottomParams());
                                break;
                            case SIMPLE:
                                cop = new SimpleCop(getGameLayout(), Common.getStickToBottomParams());
                                break;
                            default:
                                cop = new SimpleCop(getGameLayout(), Common.getStickToBottomParams());
                                break;
                        }
                        m_cops.add(cop);
                    }
                });
            }
        private void releaseDrop() {
            Drop drop=new Drop(m_layout);
            Loc rdLocation=Common.getViewLocation(getRainbowDash());
            Common.setViewLocation(getRainbowDash(),rdLocation);
            adjustDropLocToRDDirection(rdLocation);
            Common.setViewLocation(drop,rdLocation);
            getDrops().add(drop);
        }

        protected void shoot(Cop cop){
            cop.runUpdate();
            Rect rect=new Rect();
            cop.getHitRect(rect);
            final Loc hornLocation=getHornLocation(cop, rect);
//            Log.d("test","cop location: y= "+cop.getY()+" shot location: y="+y+"   x= "+x);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    addShot(hornLocation);
                }
            });
        }

        private void addShot(Loc location) {
            Shot shot = new Shot(m_layout);
            Common.setViewLocation(shot,location);
            m_shots.add(shot);
        }

        private Activity getActivity() {
            return (Activity)m_layout.getContext();
        }

        private Loc getHornLocation(Cop cop,Rect rect) {
            Loc loc=new Loc();
            loc.x=cop.getDirection()== Direction.LEFT?rect.left:rect.right;
            loc.y=rect.top;
            return loc;
        }

        public void stopGame(){
            m_IsGameRunning=false;
        }



        public ArrayList<Cop> getCops() {
            return m_cops;
        }

        public ArrayList<Drop> getDrops() {
            return m_drops;
        }

        public ArrayList<Shot> getShots() {
            return m_shots;
        }
        public void loadGame(GameSaverLoader.Game game){
            m_layout.removeAllViews();
            m_drops=game.drops;
            m_cops=game.cops;
            m_rainbowDash=game.rd;
            m_shots=getShots();
            addViewsToGame(m_drops);
            addViewsToGame(m_cops);
            addViewsToGame(m_shots);
            m_layout.addView(m_rainbowDash);

        }
        private void addViewsToGame(ArrayList arr){
            ArrayList gameArr=null;
            if(arr.get(0)==null)return;
            if(arr.get(0) instanceof Shot)gameArr=m_shots;
            else if(arr.get(0) instanceof Cop)gameArr=m_cops;
            else if(arr.get(0) instanceof Drop) gameArr=m_drops;
            if(gameArr==null)return;
            for(Object view: arr){
                m_layout.addView((GameView) view);
            }

        }*/
    }