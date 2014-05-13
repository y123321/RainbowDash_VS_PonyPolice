package il.co.ovalley.rdvsponeypolice.Runnables;

/**
 * Created by yuval on 21/04/2014.
 */
public class CopsAndDropsRunnable  {/*
    private GameRunnable m_gameRunnable;
    private volatile ArrayList<Cop> m_cops;
    private volatile ArrayList<Drop> m_drops;
    public CopsAndDropsRunnable(GameRunnable gameRunnable,ArrayList<Cop> cops,ArrayList<Drop> drops){
        m_gameRunnable=gameRunnable;
        m_cops=cops;
        m_drops=drops;
    }
    @Override
    public void run() {
        int i=0;
        while(i<m_cops.size()){
            if(i<0){
                Log.d("test","cops iterator lower then zero. its " +i);

                i=0;
            }
            if(m_cops.size()<1)return;
            Cop cop=m_cops.get(i);
            i++;
            shootIfNeeded(cop);
            updateCop(cop);
            Rect copRect=new Rect();
            cop.getHitRect(copRect);
            int j=0;
            while(j<m_drops.size()){
                if(j<0){
                    Log.d("test","drops iterator lower then zero. its " +j);

                    j=0;
                }
                if(m_drops.size()<1)return;
                Drop drop = m_drops.get(j);
                if(drop.isDead()){
                    unregister(drop);
                    j--;
                }
                else {
                    Rect dropRect = new Rect();
                    drop.getHitRect(dropRect);
                    if (dropRect.intersect(copRect)) {
                        Log.d("test", "HIT!!!");
                        killCop(cop, drop);
                        i--;
                        j--;

                    }

                }
                j++;
            }

        }
   //     updateDrops();
    }

    private void updateCop(Cop cop) {
        if (m_gameRunnable.getClockCounter() % cop.getWaitTime() == 0) cop.runViewUpdate();

    }

    private void shootIfNeeded(Cop cop) {
        if(cop.isShooting()) m_gameRunnable.shoot(cop);

    }


    protected void killCop(Cop cop, Drop drop) {
        try{
            unregister(cop);
            unregister(drop);
        }
        catch (Exception e){
            e.printStackTrace();
            killCop(cop,drop);
        }

    }

    public void unregister(Cop cop){
        Rect rect=new Rect();
        cop.getHitRect(rect);
        if(cop==null) return;
        m_gameRunnable.remove(cop);
        cop.isDead(true);
        Log.d("test","cop rect bottom:"+rect.bottom+" top: "+rect.top+" cop base: "+cop.getBaseline()+" bottom padding: "+cop.getPaddingBottom());
        cop.runViewUpdate();
        cop=null;

    }
    public void unregister(Drop drop) {
        if(drop==null) return;
        if(m_drops.contains(drop))m_drops.remove(drop);
        drop.isDead(true);
        drop.runViewUpdate();
        drop=null;
    }*/
}
