package il.co.ovalley.rdvsponeypolice.Model;

import il.co.ovalley.rdvsponeypolice.R;

/**
 * Created by yuval on 23/04/2014.
 */
public class NinjaCop extends Cop {
    public NinjaCop() {
        super(new CopDrawables(R.anim.ninja_pony__left,R.anim.ninja_pony__right));//,R.drawable.ninja_poney_left,R.drawable.ninja_poney_right));

    }

    @Override
    protected void init() {
        setShooting(false);
        setLoadingTime(8);
        setLoading(false);
        setStepsLimit(150);
        setXSpeed(5);
        setOriginalHitPoints(5);
        setScorePoints(80);
        setChanceNotToShoot(80);
    }

    @Override
    public CopType getType() {
        return CopType.NINJA;
    }
}
