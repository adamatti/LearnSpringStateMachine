package adamatti


import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.statemachine.StateContext
import org.springframework.statemachine.action.Action

@Slf4j
@CompileStatic
class SampleAction <S,E> implements Action<S, E>{
    private String message

    @Override
    void execute(StateContext<S, E> ctx) {
        log.info "${message}: ${ctx.getTarget().getId()}"
    }
}
