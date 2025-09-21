package ascii_art;

/**
 * Shell Exception classes.
 */
public class ShellExceptions {
    /**
     * Add Exception class.
     */
    public static class AddException extends Exception {
        /**
         * Exception
         * @param message exception message
         */
        public AddException(String message) {
            super(message);
        }
    }

    /**
     * Remove exception class.
     */
    static class RemoveException extends Exception {
        /**
         * Exception
         * @param message exception message
         */
        public RemoveException(String message) {
            super(message);
        }
    }

    /**
     * Resolution exception class.
     */
    static class ResolutionException extends Exception {
        /**
         * Exception
         * @param message exception message
         */
        public ResolutionException(String message) {
            super(message);
        }
    }

    /**
     * Output Exception class.
     */
    static class OutputException extends Exception {
        /**
         * Exception
         * @param message exception message
         */
        public OutputException(String message) {
            super(message);
        }
    }

    /**
     * Charset Exception class
     */
    static class CharsetException extends Exception {
        /**
         * Exception
         * @param message exception message
         */
        public CharsetException(String message) {
            super(message);
        }
    }

    /**
     * Command Exception class
     */
    static class CommandException extends Exception {
        /**
         * Exception
         * @param message exception message
         */
        public CommandException(String message) {
            super(message);
        }
    }
}
