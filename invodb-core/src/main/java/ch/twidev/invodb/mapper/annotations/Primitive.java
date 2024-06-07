package ch.twidev.invodb.mapper.annotations;

import ch.twidev.invodb.common.format.DataFormat;

public @interface Primitive {

    Class<? extends DataFormat> formatter();

}
