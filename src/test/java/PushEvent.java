import org.jeasy.states.api.AbstractEvent;

class PushEvent extends AbstractEvent {

    public PushEvent() {
        super("PushEvent");
    }

    protected PushEvent(String name) {
        super(name);
    }


}