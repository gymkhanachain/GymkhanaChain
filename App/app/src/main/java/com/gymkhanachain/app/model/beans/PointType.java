package com.gymkhanachain.app.model.beans;

import com.gymkhanachain.app.client.Point;
import com.gymkhanachain.app.client.QuizzPoint;
import com.gymkhanachain.app.client.TextPoint;

public class PointType {
    public static final String QUIZZ_POINT = "QuizzPoint";
    public static final String TEXT_POINT = "TextPointFragment";
    public static final String NONE = "";

    /**
     * Devuelve una representación en forma de String del tipo de punto
     * @param bean Un objeto de tipo PointBean
     * @return String
     */
    public static String getPointType(PointBean bean) {
        if (bean instanceof QuizzPointBean) {
            return QUIZZ_POINT;
        }

        if (bean instanceof TextPointBean) {
            return TEXT_POINT;
        }

        return NONE;
    }

    /**
     * Devuelve una representación en forma de String del tipo de punto
     * @param dto Un objeto de tipo Point
     * @return String
     */
    public static String getPointType(Point dto) {
        if (dto instanceof QuizzPoint) {
            return QUIZZ_POINT;
        }

        if (dto instanceof TextPoint) {
            return TEXT_POINT;
        }

        return NONE;
    }
}
