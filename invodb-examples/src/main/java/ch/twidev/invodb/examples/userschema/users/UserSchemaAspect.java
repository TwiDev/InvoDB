package ch.twidev.invodb.examples.userschema.users;

import ch.twidev.invodb.mapper.annotations.Async;
import ch.twidev.invodb.mapper.annotations.Update;

public interface UserSchemaAspect {

    @Update(field = "email")
    @Async
    void setEmailAsync(String email);

    @Update(field = "power")
    @Async
    void setPowerAsync(int power);

}
