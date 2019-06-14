package adamatti

import spock.lang.Specification

import static adamatti.model.SampleEvents.ACTIVATE
import static adamatti.model.SampleStates.ACTIVE
import static adamatti.model.SampleStates.INACTIVE

class StateMachineSpec extends Specification {
    def "just create"(){
        given:
            def machine = SimpleStateMachineConfiguration.build()
        when:
            machine.start()
        then:
            machine.getState().getId() == INACTIVE
    }

    def "create and set state"(){
        given:
            def machine = SimpleStateMachineConfiguration.build()
        when:
            StateMachineHelper.setState(machine,ACTIVE, ACTIVATE)
            machine.start()
        then:
            machine.getState().getId() == ACTIVE
    }

    def "send a message"(){
        given:
            def machine = SimpleStateMachineConfiguration.build()
        when:
            def result = StateMachineHelper.sendEvent(machine, ACTIVATE)
        then:
            result == true
            machine.getState().getId() == ACTIVE
    }
}
