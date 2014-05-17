package il.co.ovalley.rdvsponeypolice.Model;

import il.co.ovalley.rdvsponeypolice.R;

/**
 * Created by yuval on 15/05/2014.
 */
public class BruteCop extends Cop {
    public BruteCop() {
        super(new CopDrawables(R.drawable.brute_pony__left,R.drawable.brute_pony__right));//,R.drawable.brute_pony_shooting__left,R.drawable.brute_pony_shooting__right));
    }

    @Override
    protected void init() {
        setShooting(false);
        setLoadingTime(8);
        setLoading(false);
        setStepsLimit(150);
        setXSpeed(3);
        setOriginalHitPoints(4);
        setScorePoints(50);

    }

    @Override
    public CopType getType() {
        return CopType.BRUTE;
    }
}
