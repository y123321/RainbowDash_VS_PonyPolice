package il.co.ovalley.rdvsponeypolice.Model;

/**
 * Created by yuval on 16/04/2014.
 */
public class Drop extends GameObject {

    public Drop() {
        super();
    }

    @Override
    protected void init() {
        setYSpeed(3);
        setWaitTime(2);
    }

}