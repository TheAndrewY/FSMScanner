import org.jeasy.states.api.AbstractEvent;

class CoinEvent extends AbstractEvent {

    public CoinEvent() {
        super("CoinEvent");
    }

    protected CoinEvent(String name) {
        super(name);
    }

}
