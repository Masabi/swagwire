package acceptance.dsl

import io.swagger.client.model.SwagWiredPet

trait PetsDsl {
    SwagWiredPet rover = new SwagWiredPet()
        .name("Rover")
        .id(1234)
        .status(SwagWiredPet.StatusEnum.AVAILABLE)

}
