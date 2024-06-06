package ch.twidev.invodb.bridge.documents;

public interface OperationResult {

    OperationResult Ok = new OperationResult() {
        @Override
        public long getTime() {
            return 0;
        }
    };

    OperationResult Err = new OperationResult() {
        @Override
        public long getTime() {
            return 0;
        }
    };

    long getTime();

}
