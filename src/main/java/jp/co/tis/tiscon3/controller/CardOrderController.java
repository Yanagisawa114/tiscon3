package jp.co.tis.tiscon3.controller;

import enkan.component.BeansConverter;
import enkan.component.doma2.DomaProvider;
import enkan.data.HttpResponse;
import jp.co.tis.tiscon3.dao.CardOrderDao;
import jp.co.tis.tiscon3.entity.CardOrder;
import jp.co.tis.tiscon3.form.CardOrderForm;
import kotowari.component.TemplateEngine;
import org.codehaus.groovy.runtime.StringGroovyMethods;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.transaction.Transactional;

import static enkan.util.HttpResponseUtils.RedirectStatusCode.SEE_OTHER;
import static kotowari.routing.UrlRewriter.redirect;

/**
 * カード申し込みに関するcontrollerクラス.
 *
 * @author hirano
 */
public class CardOrderController {

    @Inject
    private TemplateEngine templateEngine;

    @Inject
    private DomaProvider daoProvider;

    @Inject
    private BeansConverter beans;

    private CardOrderDao cardOrderDao;

    @PostConstruct
    public void init() {
        cardOrderDao = daoProvider.getDao(CardOrderDao.class);
    }

    /**
     * 本人登録ページを表示します.
     *
     * @return 本人登録ページresponse
     */
    public HttpResponse inputUser() {
        return templateEngine.render("cardOrder/user", "form", new CardOrderForm());
    }

    /**
     * お勤め先登録ページを表示します.
     *
     * @return お勤め先登録ページresponse
     */
    public HttpResponse inputJob(CardOrderForm form) {
        // エラーを出したくないので強制的にエラーを消す.
        form.setErrors(null);

        return templateEngine.render("cardOrder/job", "form", form);
    }

    /**
     * 本人登録ページに戻ります.
     *
     * @return 本人登録ページresponse
     */
    public HttpResponse modifyUser(CardOrderForm form) {
        // エラーを出したくないので強制的にエラーを消す.
        form.setErrors(null);

        return templateEngine.render("cardOrder/user", "form", form);
    }

    /**
     * カード申し込み情報をDatabaseに登録します.
     *
     * @return 完了ページへのリダイレクトresponse
     */
    @Transactional
    public HttpResponse create(CardOrderForm form) {
        if (form.hasErrors()) {
            return templateEngine.render("cardOrder/user", "form", form);
        }




        CardOrder cardOrder = beans.createFrom(form, CardOrder.class);
        String sei = form.getKanjiSei();
        String mei = form.getKanjiMei();
        cardOrder.setKanjiName(sei+mei);
        String sei2 = form.getKanaSei();
        String mei2 = form.getKanaMei();
        cardOrder.setKanaName(sei2+mei2);
        String sei3 = form.getAlphabetSei();
        String mei3 = form.getAlphabetMei();
        cardOrder.setAlphabetName(sei3+mei3);

        String Birth1 = form.getDateOfBirth1();
        String Birth2 = form.getDateOfBirth2();
        String Birgh3 = form.getDateOfBirth3();
        cardOrder.setDateOfBirth(Birth1+Birth2+Birgh3);
        String zip1 = form.getZipCode1();
        String zip2 = form.getZipCode2();
        cardOrder.setZipCode(zip1+zip2);
        String home1 = form.getHomePhoneNumber1();
        String home2 = form.getHomePhoneNumber2();
        String home3 = form.getHomePhoneNumber3();
        cardOrder.setHomePhoneNumber(home1+home2+home3);
        String mobile1 = form.getMobilePhoneNumber1();
        String mobile2 = form.getMobilePhoneNumber2();
        String mobeil3 = form.getMobilePhoneNumber3();
        cardOrder.setHomePhoneNumber(mobile1+mobile2+mobeil3);
        String mail1 = form.getEmailAddress1();
        String mail2 = form.getEmailAddress2();
        cardOrder.setEmailAddress(mail1+mail2);
        String ezip1 = form.getEmployerZipCode1();
        String ezip2 = form.getEmployerZipCode2();
        cardOrder.setEmployerZipCode(ezip1+ezip2);
        String emobile1 = form.getEmployerPhoneNumber1();
        String emobile2 = form.getEmployerPhoneNumber2();
        String emobeil3 = form.getEmployerPhoneNumber3();
        cardOrder.setEmployerPhoneNumber(emobile1+emobile2+emobeil3);



        cardOrderDao.insert(cardOrder);

        return redirect(getClass(), "completed", SEE_OTHER);
    }

    /**
     * 完了ページを表示します.
     *
     * @return 完了ページresponse
     */
    public HttpResponse completed() {
        return templateEngine.render("cardOrder/completed");
    }

}
