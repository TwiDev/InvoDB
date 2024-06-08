package ch.twidev.invodb.repository.annotations;

public @interface FindOrInsert {

    Find find();
    Insert insert();

}
