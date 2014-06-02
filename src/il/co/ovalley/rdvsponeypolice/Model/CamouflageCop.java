package il.co.ovalley.rdvsponeypolice.Model;

import il.co.ovalley.rdvsponeypolice.R;

/**
 * Created by yuval on 16/05/2014.
 */
public class CamouflageCop extends Cop {
    public CamouflageCop() {
        super(new CopDrawables(R.anim.camouflage_pony__left,R.anim.camouflage_pony__right));
    }

    @Override
    public CopType getType() {
        return CopType.CAMOUFLAGE;
    }

    @Override
    protected void init() {
        setOriginalHitPoints(3);
        setScorePoints(55);
        setXSpeed(3.5f);
        setStepsLimit(200);
        setChanceNotToShoot(130);

    }
}
