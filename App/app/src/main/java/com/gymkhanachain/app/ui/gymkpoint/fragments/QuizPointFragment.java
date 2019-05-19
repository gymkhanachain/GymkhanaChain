package com.gymkhanachain.app.ui.gymkpoint.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gymkhanachain.app.R;
import com.gymkhanachain.app.commons.ProxyBitmap;
import com.gymkhanachain.app.model.beans.QuizzPointBean;
import com.gymkhanachain.app.model.commons.PointCache;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QuizPointFragment extends Fragment implements ProxyBitmap.OnProxyBitmapListener {

    private static final String ARG_POINT_ID = "PointId";

    private static final PointCache pointCache = PointCache.getInstance();

    Integer pointId;
    OnQuizPointFragmentInteraction listener;
    QuizzPointBean bean;

    @BindView(R.id.pointImage)
    ImageView ivPointImage;

    @BindView(R.id.pointName)
    TextView tvPointName;

    @BindView(R.id.pointQuestionTextView)
    TextView tvPointQuestion;

    @BindView(R.id.answerButton_1)
    Button btnQuestion1;

    @BindView(R.id.answerButton_2)
    Button btnQuestion2;

    @BindView(R.id.answerButton_3)
    Button btnQuestion3;

    @BindView(R.id.answerButton_4)
    Button btnQuestion4;

    public QuizPointFragment() {
        // Required empty public constructor
    }

    /**
     * Crea un fragmento de tipo QuizPointFragment
     *
     * @param pointId Id del punto.
     * @return Nueva instancia de QuizPointFragment.
     */
    public static QuizPointFragment newInstance(Integer pointId) {
        QuizPointFragment fragment = new QuizPointFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POINT_ID, pointId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pointId = getArguments().getInt(ARG_POINT_ID);
            bean = (QuizzPointBean) pointCache.getPoint(pointId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quiz_point, container, false);
        ButterKnife.bind(this, view);

        if (bean.getImage().getBitmap() != null) {
            ivPointImage.setImageBitmap(bean.getImage().getBitmap());
        } else {
            bean.getImage().attach(this);
        }

        tvPointName.setText(bean.getName());
        tvPointQuestion.setText(bean.getQuestion());

        btnQuestion1.setText(bean.getSolutions().get(0));
        btnQuestion1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bean.getSolution() == 0) {
                    listener.onCorrectQuestion();
                } else {
                    listener.onIncorrectQuestion(bean.getSolutions().get(bean.getSolution()));
                }
            }
        });

        btnQuestion2.setText(bean.getSolutions().get(1));
        btnQuestion2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bean.getSolution() == 1) {
                    listener.onCorrectQuestion();
                } else {
                    listener.onIncorrectQuestion(bean.getSolutions().get(bean.getSolution()));
                }
            }
        });

        btnQuestion3.setText(bean.getSolutions().get(2));
        btnQuestion3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bean.getSolution() == 2) {
                    listener.onCorrectQuestion();
                } else {
                    listener.onIncorrectQuestion(bean.getSolutions().get(bean.getSolution()));
                }
            }
        });

        btnQuestion4.setText(bean.getSolutions().get(3));
        btnQuestion4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bean.getSolution() == 3) {
                    listener.onCorrectQuestion();
                } else {
                    listener.onIncorrectQuestion(bean.getSolutions().get(bean.getSolution()));
                }
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnQuizPointFragmentInteraction) {
            listener = (OnQuizPointFragmentInteraction) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        bean.getImage().detach(this);
        listener = null;
    }

    @Override
    public void onBitmapChange(ProxyBitmap bitmap) {
        ivPointImage.setImageBitmap(bitmap.getBitmap());
    }

    public interface OnQuizPointFragmentInteraction {
        void onCorrectQuestion();
        void onIncorrectQuestion(String correctAnswer);
    }
}
