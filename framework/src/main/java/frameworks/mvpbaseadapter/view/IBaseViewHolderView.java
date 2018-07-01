package frameworks.mvpbaseadapter.view;
import frameworks.basemvp.IBaseView;
/**
 * Created by sandeep on 31/7/17.
 */

public interface IBaseViewHolderView  {
    public void linkTo(int position);

    int getLinkedPosition();
}
