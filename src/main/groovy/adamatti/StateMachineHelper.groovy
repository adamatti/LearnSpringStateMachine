package adamatti


import groovy.transform.CompileStatic
import org.springframework.statemachine.StateMachine
import org.springframework.statemachine.access.StateMachineAccess
import org.springframework.statemachine.access.StateMachineFunction
import org.springframework.statemachine.support.DefaultStateMachineContext

@CompileStatic
class StateMachineHelper {
    // Set to a specific state
    static <S,E> void setState(
        StateMachine<S, E> stateMachine,
        S state,
        E event
    ){
        def headers = [:]

        def context = new DefaultStateMachineContext(
            state,
            event,
            headers,
            stateMachine.getExtendedState()
        )

        def stateMachineFunction = new StateMachineFunction<StateMachineAccess<S, E>>() {
            @Override
            void apply(final StateMachineAccess<S, E> function) {
                function.resetStateMachine(context)
            }
        }

        stateMachine.getStateMachineAccessor().doWithAllRegions(stateMachineFunction);
    }

    static <S,E> boolean sendEvent(StateMachine<S,E> stateMachine, E event){
        StateMachineListener listener = new StateMachineListener<S,E>()
        stateMachine.addStateListener(listener)

        stateMachine.start()

        def previousState = stateMachine.getState().getId()
        boolean stateMachineIsCompleteBeforeSendEvent = stateMachine.isComplete() // || previousState.isFinalState()
        if (!stateMachineIsCompleteBeforeSendEvent) {
            stateMachine.sendEvent(event)
        }

        stateMachine.stop()

        (
            !stateMachineIsCompleteBeforeSendEvent &&
                listener.getEventAccepted() &&
                !stateMachine.hasStateMachineError()
        )
    }
}
