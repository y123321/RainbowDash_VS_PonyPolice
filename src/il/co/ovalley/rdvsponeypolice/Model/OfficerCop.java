package il.co.ovalley.rdvsponeypolice.Model;

import il.co.ovalley.rdvsponeypolice.R;

/**
 * Created by yuval on 30/05/2014.
 */
public class OfficerCop extends Cop {
    public OfficerCop() {
        super(new CopDrawables(R.anim.ninja_pony__left,R.anim.simple_cop__right));
    }

    @Override
    public CopType getType() {
        return null;
       // return CopType.OFFICER;
    }

    @Override
    protected void init() {
        setShooting(false);
        setLoadingTime(7);
        setLoading(false);
        setStepsLimit(90);
        setXSpeed(2.1f);
        setChanceNotToShoot(150);
        setOriginalHitPoints(3);
    }
}
