public class StateChain {
    private State currentState;
    private String name;

    public StateChain() {                 // constructor method
        currentState = new UncheckedState();            // program will start UncheckedState as default
    }

    public void set_state(State s) {    // parameter state will be currentState
        currentState = s;
    }

    public void doAction() {             // currentState is updating
        currentState.update(this);
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
}
