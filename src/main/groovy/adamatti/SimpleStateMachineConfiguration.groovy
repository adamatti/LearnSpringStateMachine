package adamatti

import adamatti.model.SampleEvents
import adamatti.model.SampleStates
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Configuration
import org.springframework.statemachine.StateMachine
import org.springframework.statemachine.config.EnableStateMachine
import org.springframework.statemachine.config.StateMachineBuilder
import org.springframework.statemachine.config.StateMachineConfigurerAdapter
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer


/**
 * https://projects.spring.io/spring-statemachine/
 * https://www.baeldung.com/spring-state-machine
 * https://docs.spring.io/spring-statemachine/docs/current/reference/htmlsingle/
 */
@Slf4j
@CompileStatic
@Configuration
@EnableStateMachine
class SimpleStateMachineConfiguration extends StateMachineConfigurerAdapter<SampleStates, SampleEvents> {
    @Override
    void configure(StateMachineStateConfigurer<SampleStates, SampleEvents> config){
        config.withStates()
            .initial(SampleStates.INACTIVE)
            .states(EnumSet.allOf(SampleStates.class))
    }

    @Override
    void configure(StateMachineTransitionConfigurer<SampleStates, SampleEvents> config){
        config.withExternal()
            .source(SampleStates.ACTIVE)
            .target(SampleStates.INACTIVE)
            .event(SampleEvents.DEACTIVATE)
        .and().withExternal()
            .source(SampleStates.INACTIVE)
            .target(SampleStates.ACTIVE)
            .event(SampleEvents.ACTIVATE)
            .action(new SampleAction(message: "Activating"))
    }

    static StateMachine<SampleStates,SampleEvents> build(ApplicationContext context = null){
        def config = new SimpleStateMachineConfiguration()

        StateMachineBuilder.Builder<SampleStates,SampleEvents> builder = StateMachineBuilder.builder()

        def configurer = builder.configureConfiguration()
            .withConfiguration()
            .autoStartup(false)

        if (context) {
            configurer.beanFactory(context.getAutowireCapableBeanFactory())
        }

        config.configure(builder.configureStates())
        config.configure(builder.configureTransitions())

        builder.build()
    }
}




