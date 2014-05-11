package il.co.ovalley.rdvsponeypolice.Model;

/**
 * Created by yuval on 20/04/2014.
 */
public class Shot extends GameObject {
    public Shot() {
        super();
    }

    @Override
    protected void init() {
        setWaitTime(3);
        setYSpeed(3);
    }


}
