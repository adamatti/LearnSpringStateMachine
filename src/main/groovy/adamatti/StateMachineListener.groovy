package adamatti


import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.messaging.Message
import org.springframework.statemachine.listener.StateMachineListenerAdapter
import org.springframework.statemachine.state.State
import org.springframework.stereotype.Component

@Slf4j
@Component
@CompileStatic
class StateMachineListener <S,E> extends StateMachineListenerAdapter<S, E> {
    private boolean eventAccepted = true

    boolean getEventAccepted(){
        eventAccepted
    }

    @Override
    void eventNotAccepted(final Message<E> event) {
        eventAccepted = false
    }

    @Override
    void stateChanged(State<S, E> from, State<E, E> to) {
        log.info "Changed from ${from?.getId()} to ${to?.getId()}"
    }
}
