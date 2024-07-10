package ch.twidev.invodb.examples.messages;

import ch.twidev.invodb.repository.annotations.Update;

public interface MessageAspect {

    @Update(field = "content")
    void setContent(String msg);

}
