package il.co.ovalley.rdvsponeypolice.Model;

import il.co.ovalley.rdvsponeypolice.R;

/**
 * Created by yuval on 24/04/2014.
 */
public class SimpleCop extends Cop {
    public SimpleCop() {
        super(new CopDrawables(R.anim.simple_cop__left,R.anim.simple_cop__right));//,R.drawable.police_pony_shooting_left,R.drawable.police_pony_shooting_right));
    }

    @Override
    protected void init() {
        setShooting(false);
        setLoadingTime(10);
        setLoading(false);
        setStepsLimit(100);
        setXSpeed(1.5f);
        setChanceNotToShoot(200);

    }

    @Override
    public CopType getType() {
        return CopType.SIMPLE;
    }
}
