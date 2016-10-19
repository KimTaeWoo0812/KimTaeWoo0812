package duclass.duclass.a0_SplashPackage;

/**
 * Created by Seongho on 2016-03-26.
 */
public class IncompatibleRatioException extends RuntimeException {
    private static final long serialVersionUID = 234608108593115395L;

    public IncompatibleRatioException() {
        super("Can't perform Ken Burns effect on rects with distinct aspect ratios!");
    }
}
