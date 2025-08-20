package br.com.helpdesk.userserviceapi.creator;

import lombok.experimental.UtilityClass;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@UtilityClass
public class CreatorUtil {

    private static final PodamFactory factory = new PodamFactoryImpl();

    public static <T> T generateMock(Class<T> clazz) {
        return factory.manufacturePojo(clazz);
    }

}
