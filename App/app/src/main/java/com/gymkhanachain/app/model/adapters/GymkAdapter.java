package com.gymkhanachain.app.model.adapters;

import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;
import com.gymkhanachain.app.client.Gymkhana;
import com.gymkhanachain.app.client.Point;
import com.gymkhanachain.app.client.QuizzPoint;
import com.gymkhanachain.app.client.TextPoint;
import com.gymkhanachain.app.commons.ProxyBitmap;
import com.gymkhanachain.app.commons.asynctasks.DownloadImageAsyncTask;
import com.gymkhanachain.app.model.beans.GymkhanaBean;
import com.gymkhanachain.app.model.beans.PointBean;
import com.gymkhanachain.app.model.beans.PointType;
import com.gymkhanachain.app.model.beans.QuizzPointBean;
import com.gymkhanachain.app.model.beans.TextPointBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GymkAdapter {

    /**
     * Transforma un objeto Gymkhana a un GymkhanaBean
     * @param dto Objeto de la base de datos
     * @return GymkhanaBean
     */
    public static GymkhanaBean adapt(Gymkhana dto) {
        List<PointBean> points = new ArrayList<>();

        for (Point point : dto.getPuntos()) {
            points.add(adapt(point));
        }

        final LatLng position = new LatLng(dto.getLatitude(), dto.getLongitude());

        Uri uri = Uri.parse(dto.getImage());
        ProxyBitmap proxyBitmap = new ProxyBitmap();
        DownloadImageAsyncTask asyncTask = new DownloadImageAsyncTask(proxyBitmap);
        asyncTask.execute(uri);

        final Date createDate = new Date();
        createDate.setTime(dto.getCrea_time());

        final Date openDate = new Date();
        openDate.setTime(dto.getAper_time());

        final Date closeDate = new Date();
        closeDate.setTime(dto.getClose_time());

        return new GymkhanaBean(dto.getGymk_id(), dto.getName(), dto.getDescription(),
                dto.getType(), dto.isA11y(), dto.isPriv(), dto.getAcc_cod(), dto.getCreator(),
                createDate, openDate, closeDate, proxyBitmap, position, points);
    }

    /**
     * Transforma un objeto GymkhanaBean a un Gymkhana
     * @param bean El bean de la gymkana
     * @return Gymkhana
     */
    public static Gymkhana adapt(GymkhanaBean bean) {
        List<Point> points = new ArrayList<>();

        for (PointBean point : bean.getPoints()) {
            points.add(adapt(point));
        }

        int createDate = Long.valueOf(bean.getCreateDate().getTime()).intValue();
        int openDate = Long.valueOf(bean.getOpenDate().getTime()).intValue();
        int closeDate = Long.valueOf(bean.getCloseDate().getTime()).intValue();

        return new Gymkhana(bean.getId(), bean.getImage().getBitmap(), bean.getName(),
                bean.getDescription(), bean.getPosition(), bean.getType(), bean.getAccessibility(),
                bean.getPrivGymk(), bean.getAccessCode(), bean.getCreator(), createDate, openDate,
                closeDate, points);
    }

    /**
     * Transforma un objeto Point en un objeto PointBean
     * @param dto Objeto de la base de datos
     * @return PointBean
     */
    public static PointBean adapt(Point dto) {
        Uri uri = Uri.parse(dto.getImage());

        ProxyBitmap proxyBitmap = new ProxyBitmap();
        DownloadImageAsyncTask asyncTask = new DownloadImageAsyncTask(proxyBitmap);
        asyncTask.execute(uri);

        final LatLng position = new LatLng(dto.getLatitude(), dto.getLongitude());

        switch (PointType.getPointType(dto)) {
            case PointType.QUIZZ_POINT:
                return adaptQuizzPointToBean(dto, proxyBitmap);
            case PointType.TEXT_POINT:
                return adaptTextPointToBean(dto, proxyBitmap);
            default:
                throw new RuntimeException("Point adapter not defined: " + dto.getClass().getName());
        }
    }

    private static QuizzPointBean adaptQuizzPointToBean(Point dto, ProxyBitmap proxyBitmap) {
        QuizzPoint quizzPoint = (QuizzPoint) dto;

        List<String> solutions = new ArrayList<>();
        solutions.add(quizzPoint.getSol1());
        solutions.add(quizzPoint.getSol2());
        solutions.add(quizzPoint.getSol3());
        solutions.add(quizzPoint.getSol4());

        return new QuizzPointBean(quizzPoint.getPoint_id(), quizzPoint.getName(),
                quizzPoint.getDescription(), proxyBitmap, quizzPoint.getPosition(),
                quizzPoint.getQuizz_text(), solutions, quizzPoint.getSolution());
    }

    private static TextPointBean adaptTextPointToBean(Point dto, ProxyBitmap proxyBitmap) {
        TextPoint textPoint = (TextPoint) dto;

        return new TextPointBean(textPoint.getPoint_id(), textPoint.getName(),
                textPoint.getDescription(), proxyBitmap, textPoint.getPosition(),
                textPoint.getLong_desc());
    }

    /**
     * Transforma un objeto PointBean a un Point
     * @param bean El bean del punto
     * @return Point
     */
    public static Point adapt(PointBean bean) {
        switch (PointType.getPointType(bean)) {
            case PointType.QUIZZ_POINT:
                return adaptQuizzPointBeanToDto(bean);
            case PointType.TEXT_POINT:
                return adaptTextPointBeanToDto(bean);
            default:
                throw new RuntimeException("Point adapter not defined: " + bean.getClass().getName());
        }
    }

    private static QuizzPoint adaptQuizzPointBeanToDto(PointBean bean) {
        QuizzPointBean quizzPoint = (QuizzPointBean) bean;

        return new QuizzPoint(quizzPoint.getId(), quizzPoint.getImage().getBitmap(),
                quizzPoint.getName(), quizzPoint.getDescription(), quizzPoint.getPosition(),
                quizzPoint.getQuestion(), quizzPoint.getSolutions().get(0),
                quizzPoint.getSolutions().get(1), quizzPoint.getSolutions().get(2),
                quizzPoint.getSolutions().get(3), quizzPoint.getSolution());
    }

    private static TextPoint adaptTextPointBeanToDto(PointBean bean) {
        TextPointBean textPoint = (TextPointBean) bean;

        return new TextPoint(textPoint.getId(), textPoint.getImage().getBitmap(),
                textPoint.getName(), textPoint.getDescription(), textPoint.getPosition(),
                textPoint.getLongDescription());
    }
}
