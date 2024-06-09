package ch.twidev.invodb.examples.userschema.users;

import ch.twidev.invodb.common.format.JsonFormatter;
import ch.twidev.invodb.mapper.annotations.Async;
import ch.twidev.invodb.mapper.annotations.Primitive;
import ch.twidev.invodb.mapper.annotations.Update;

public interface UserSchemaAspect {

    @Update(field = "email")
    @Async
    void setEmailAsync(String email);

    @Update(field = "power")
    @Async
    void setPowerAsync(int power);

    @Update(field = "userSchema")
    @Primitive(formatter = JsonFormatter.class)
    void setData(UserData userData);

}
