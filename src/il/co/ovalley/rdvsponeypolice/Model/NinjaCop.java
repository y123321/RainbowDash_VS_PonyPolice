package il.co.ovalley.rdvsponeypolice.Model;

import il.co.ovalley.rdvsponeypolice.R;

/**
 * Created by yuval on 23/04/2014.
 */
public class NinjaCop extends Cop {
    public NinjaCop() {
        super(new CopDrawables(R.drawable.ninja_poney_left,R.drawable.ninja_poney_right,R.drawable.ninja_poney_left,R.drawable.ninja_poney_right));

    }

    @Override
    protected void init() {
        setShooting(false);
        setLoadingTime(8);
        setLoading(false);
        setStepsLimit(150);
        setXSpeed(3);
        setType(CopType.NINJA);
    }
}
