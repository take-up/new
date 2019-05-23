package hello.test.event;

/**
 * 消息驱动
 */
public class BaseEvent {

    private int action;

    public BaseEvent( int action) {
        this.action = action;
    }

    public int getAction() {
        return action;
    }
}
