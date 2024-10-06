import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.jeasy.states.api.State;
import org.jeasy.states.api.Transition;
import org.jeasy.states.core.FiniteStateMachineBuilder;
import org.jeasy.states.core.TransitionBuilder;

import java.io.IOException;
import java.util.*;

public class FSMD extends JsonDeserializer<FiniteStateMachineBuilder> {

    @Override
    public FiniteStateMachineBuilder deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException{
        ObjectMapper om = new ObjectMapper();
        JsonNode jn = om.readTree(jp);
        ArrayList<Transition> transitions = new ArrayList<>();
        String currentName = "";
        String initialName = "";
        State curr,initial = null;
        Set<State> states = new HashSet<>();
        //Iterate through the JSON text
        for (Iterator<String> it = jn.fieldNames(); it.hasNext(); ) {
            String i = it.next();
            //Encountering states
            if(i.equals("states")){
                ArrayNode ja = (ArrayNode) jn.get(i);
                //For every state, create a state object with name and save to states set
                for (JsonNode x : ja){
                    states.add(new State(x.get("name").asText()));
                }
                //Transitions
            }else if(i.equals("transitions")){
                ArrayNode ja = (ArrayNode) jn.get(i);
                //Go through every transition in JSON file
                for (JsonNode x : ja){
                    State sState = null;
                    State tState = null;
                    // Search through states set to find matching source and target states
                    // and save those aliases for later
                    for( State s : states){
                        if(s.getName().equals(x.get("sourceState").get("name").asText())){
                            sState = s;
                        }
                        if(s.getName().equals(x.get("targetState").get("name").asText())){
                            tState = s;
                        }
                    }
                    // Build transition and save to list of all transitions
                    Transition t = new TransitionBuilder()
                            .name(x.get("name").asText())
                            .sourceState(sState)
                            .targetState(tState)
                            .build();
                    transitions.add(t);
                }
            }
        }
        // At the end, compile everything we have and create the FSM builder based on JSON file
        FiniteStateMachineBuilder builder = new FiniteStateMachineBuilder(states, initial);
        for (Transition t : transitions){
            builder.registerTransition(t);
        }
        return builder;
    }
}