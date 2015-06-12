package agency.alterway.edillion.views.adapters;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.List;

import agency.alterway.edillion.EdillionApplication;
import agency.alterway.edillion.R;
import agency.alterway.edillion.controllers.ScanController;
import agency.alterway.edillion.models.DocumentFile;
import agency.alterway.edillion.utils.PressMode;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 *
 * Created by marekrigan on 22/05/15.
 */
public class DocumentsAdapter extends RecyclerView.Adapter<DocumentsAdapter.ViewHolder> {

    private PressMode          pressMode;
    private ActivityCallback   callback;
    private ViewHolder         previouslySelected;
    private List<DocumentFile> documents;

    public DocumentsAdapter(ActivityCallback callback, List<DocumentFile> documents)
    {
        super();

        this.callback = callback;
        this.documents = documents;

        pressMode = PressMode.NONE_SELECTED;
        previouslySelected = null;
    }

    public void setMode(PressMode mode)
    {
        if(previouslySelected != null)
        {
            previouslySelected.colorTransition(true, previouslySelected.flipper, previouslySelected.detailsLayout, previouslySelected.imgThumbnail, previouslySelected.description);
            previouslySelected = null;
        }
        this.pressMode = mode;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.document_card, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position)
    {
        DocumentFile file = documents.get(position);

        viewHolder.viewLayout.setBackgroundResource(R.color.icons);

        switch (pressMode)
        {
            case LONG_SELECTED:
                viewHolder.checkDelete.setVisibility(View.VISIBLE);
                break;
            case NONE_SELECTED:
                viewHolder.checkDelete.setVisibility(View.GONE);
                viewHolder.checkDelete.setChecked(false);
                break;
            case ALL_SELECTED:
                viewHolder.checkDelete.setVisibility(View.VISIBLE);
                viewHolder.checkDelete.setChecked(true);
                break;
        }
        if(file.getDescription() != null)
        {
            viewHolder.description.setText(file.getDescription());
        }
        ScanController.getInstance().setImageOnView(file.getThumbnail(),viewHolder.imgThumbnail);
        viewHolder.detailsLayout.setBackgroundResource(R.color.material_blue_grey_800);

    }

    @Override
    public int getItemCount() {

        return documents.size();
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener
    {
        public View itemView;
        private static final int PICTURE = 0;
        private static final int DETAILS = 1;

        @InjectView(R.id.flipper_document)
        ViewFlipper flipper;

        @InjectView(R.id.view_layout)
        FrameLayout viewLayout;
        @InjectView(R.id.check_delete)
        CheckBox    checkDelete;
        @InjectView(R.id.thumbnail_grid)
        ImageView   imgThumbnail;
        @InjectView(R.id.description_grid)
        TextView    description;

        @InjectView(R.id.details_layout)
        LinearLayout detailsLayout;
        @InjectView(R.id.view_document)
        ImageButton  viewDocument;
        @InjectView(R.id.approve_document)
        ImageButton  approveDocument;
        @InjectView(R.id.delete_document)
        ImageButton  deleteDocument;

        public ViewHolder(View itemView)
        {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.inject(this, itemView);

            viewDocument.setOnClickListener(this);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            try
            {
                switch (v.getId())
                {
                    case R.id.view_document:
                        callback.goViewPhoto(getAdapterPosition());
                        break;
                    default:
                        switch (pressMode)
                        {
                            case NONE_SELECTED:
                                colorTransition(false, flipper, viewLayout, imgThumbnail, description);
                                pressMode = PressMode.SELECTED;
                                previouslySelected = this;
                                break;
                            case SELECTED:
                                if (previouslySelected.getAdapterPosition() != this.getAdapterPosition())
                                {
                                    colorTransition(true, previouslySelected.flipper, previouslySelected.detailsLayout, previouslySelected.imgThumbnail, previouslySelected.description);
                                    previouslySelected = this;
                                    colorTransition(false, flipper, viewLayout, imgThumbnail, description);
                                }
                                else
                                {
                                    colorTransition(true, flipper, detailsLayout, imgThumbnail, description);
                                    pressMode = PressMode.NONE_SELECTED;
                                    previouslySelected = null;
                                }
                                break;
                            case LONG_SELECTED:
                                checkDelete.setChecked(!checkDelete.isChecked());
                                callback.isLongSelected(false);
                                break;
                            case ALL_SELECTED:
                                checkDelete.setChecked(!checkDelete.isChecked());
                                callback.isLongSelected(true);
                                callback.isNotAllSelected();
                                pressMode = PressMode.LONG_SELECTED;
                                break;
                            case UNKNOWN:
                                throw new Exception();
                        }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        @Override
        public boolean onLongClick(View v)
        {
            try
            {
                switch (pressMode)
                {
                    case NONE_SELECTED:
                        pressMode = PressMode.LONG_SELECTED;
                        notifyDataSetChanged();
                        checkDelete.setChecked(true);
                        callback.isLongSelected(true);
                        return true;
                    case SELECTED:
                        colorTransition(true,previouslySelected.flipper,previouslySelected.detailsLayout,previouslySelected.imgThumbnail,previouslySelected.description);
                        previouslySelected = null;

                        pressMode = PressMode.LONG_SELECTED;
                        notifyDataSetChanged();
                        checkDelete.setChecked(true);
                        callback.isLongSelected(true);
                        return true;
                    case LONG_SELECTED:
                    case ALL_SELECTED:
                        checkDelete.setChecked(false);
                        pressMode = PressMode.NONE_SELECTED;
                        notifyDataSetChanged();
                        callback.isLongSelected(false);
                        callback.isNotAllSelected();
                        return true;
                    case UNKNOWN:
                        throw new Exception();
                    default:
                        throw new Exception();
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
                return false;
            }
        }

        public void colorTransition(boolean hasBeenPressed, final ViewFlipper flipper, final View view, final ImageView image, final TextView description)
        {
            Integer colorFrom;
            Integer colorTo;

            Integer       textColor = EdillionApplication.getAppContext().getResources().getColor(R.color.icons);
            ValueAnimator textColorAnimation;

            final int destination;
            if (hasBeenPressed)
            {
                colorFrom = EdillionApplication.getAppContext().getResources().getColor(R.color.material_blue_grey_800);
                colorTo = EdillionApplication.getAppContext().getResources().getColor(android.R.color.transparent);
                textColorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, textColor);
                destination = PICTURE;
            }
            else
            {
                colorFrom = EdillionApplication.getAppContext().getResources().getColor(android.R.color.transparent);
                colorTo = EdillionApplication.getAppContext().getResources().getColor(R.color.material_blue_grey_800);
                textColorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), textColor, colorTo);
                destination = DETAILS;
            }

            textColorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
            {

                @Override
                public void onAnimationUpdate(ValueAnimator animator)
                {
                    view.setBackgroundColor((Integer) animator.getAnimatedValue());
                    description.setTextColor((Integer) animator.getAnimatedValue());
                }

            });
            textColorAnimation.addListener(new Animator.AnimatorListener()
            {
                @Override
                public void onAnimationStart(Animator animation)
                {

                }

                @Override
                public void onAnimationEnd(Animator animation)
                {
                    flipper.setDisplayedChild(destination);
                    viewLayout.setBackgroundResource(R.color.icons);
                    detailsLayout.setBackgroundResource(R.color.material_blue_grey_800);
                }

                @Override
                public void onAnimationCancel(Animator animation)
                {

                }

                @Override
                public void onAnimationRepeat(Animator animation)
                {

                }
            });
            textColorAnimation.start();

            ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
            colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    image.setColorFilter((Integer) animator.getAnimatedValue());
                }

            });
            colorAnimation.addListener(new Animator.AnimatorListener()
            {
                @Override
                public void onAnimationStart(Animator animation)
                {

                }

                @Override
                public void onAnimationEnd(Animator animation)
                {
                    flipper.setDisplayedChild(destination);
                    viewLayout.setBackgroundResource(R.color.icons);
                    detailsLayout.setBackgroundResource(R.color.material_blue_grey_800);
                }

                @Override
                public void onAnimationCancel(Animator animation)
                {

                }

                @Override
                public void onAnimationRepeat(Animator animation)
                {

                }
            });
            colorAnimation.start();
        }
    }

    public interface ActivityCallback
    {
        void isLongSelected(boolean isSelected);
        void isNotAllSelected();
        void goViewPhoto(int position);
    }
}
