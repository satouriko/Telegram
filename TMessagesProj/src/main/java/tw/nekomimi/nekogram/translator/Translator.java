package tw.nekomimi.nekogram.translator;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import androidx.core.util.Pair;

import org.json.JSONException;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.tgnet.TLRPC;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import tw.nekomimi.nekogram.NekoConfig;

abstract public class Translator {
    public static final int PROVIDER_GOOGLE = 1;
    public static final int PROVIDER_GOOGLE_CN = 2;
    public static final int PROVIDER_LINGO = 3;
    public static final int PROVIDER_YANDEX = 4;
    public static final int PROVIDER_DEEPL = 5;
    public static final int PROVIDER_YOUDAO = 6;
    public static final int PROVIDER_MICROSOFT = 7;

    public static Pair<ArrayList<String>, ArrayList<Integer>> getProviders() {
        ArrayList<String> names = new ArrayList<>();
        ArrayList<Integer> types = new ArrayList<>();
        names.add(LocaleController.getString("ProviderGoogleTranslate", R.string.ProviderGoogleTranslate));
        types.add(Translator.PROVIDER_GOOGLE);
        names.add(LocaleController.getString("ProviderGoogleTranslateCN", R.string.ProviderGoogleTranslateCN));
        types.add(Translator.PROVIDER_GOOGLE_CN);
        names.add(LocaleController.getString("ProviderLingocloud", R.string.ProviderLingocloud));
        types.add(Translator.PROVIDER_LINGO);
        names.add(LocaleController.getString("ProviderYandex", R.string.ProviderYandex));
        types.add(Translator.PROVIDER_YANDEX);
        names.add(LocaleController.getString("ProviderDeepLTranslate", R.string.ProviderDeepLTranslate));
        types.add(Translator.PROVIDER_DEEPL);
        names.add(LocaleController.getString("ProviderYouDao", R.string.ProviderYouDao));
        types.add(Translator.PROVIDER_YOUDAO);
        names.add(LocaleController.getString("ProviderMicrosoftTranslator", R.string.ProviderMicrosoftTranslator));
        types.add(Translator.PROVIDER_MICROSOFT);
        return new Pair<>(names, types);
    }

    public static void translate(Object query, TranslateCallBack translateCallBack) {
        Translator translator;
        int provider = NekoConfig.translationProvider;
        switch (provider) {
            case PROVIDER_YANDEX:
                translator = YandexTranslator.getInstance();
                break;
            case PROVIDER_LINGO:
                translator = LingoTranslator.getInstance();
                break;
            case PROVIDER_DEEPL:
                translator = DeepLTranslator.getInstance();
                break;
            case PROVIDER_YOUDAO:
                translator = YouDaoTranslator.getInstance();
                break;
            case PROVIDER_MICROSOFT:
                translator = MicrosoftTranslator.getInstance();
                break;
            case PROVIDER_GOOGLE:
            case PROVIDER_GOOGLE_CN:
            default:
                translator = GoogleAppTranslator.getInstance();
                break;
        }

        List<String> targetLanguages = translator.getTargetLanguages();
        Locale locale = LocaleController.getInstance().currentLocale;
        String toLang = convertLanguageCode(provider, locale.getLanguage(), locale.getCountry());
        if (!targetLanguages.contains(toLang)) {
            toLang = convertLanguageCode(provider, LocaleController.getString("LanguageCode", R.string.LanguageCode), null);
        }
        if (!targetLanguages.contains(toLang)) {
            translateCallBack.onUnsupported();
        } else {
            translator.startTask(query, toLang, translateCallBack);
        }
    }


    private void startTask(Object query, String toLang, TranslateCallBack translateCallBack){
            new MyAsyncTask().request(query, toLang, translateCallBack).execute();
    }

    private static String convertLanguageCode(int provider, String language, String country) {
        String toLang;
        switch (provider) {
            case PROVIDER_YANDEX:
            case PROVIDER_LINGO:
                toLang = language;
                break;
            case PROVIDER_DEEPL:
                toLang = language.toUpperCase();
                break;
            case PROVIDER_YOUDAO:
                if (language.equals("zh")) {
                    toLang = "zh-CHS";
                } else {
                    toLang = language;
                }
                break;
            case PROVIDER_MICROSOFT:
            case PROVIDER_GOOGLE:
            case PROVIDER_GOOGLE_CN:
            default:
                if (country != null && language.equals("zh")) {
                    String countryUpperCase = country.toUpperCase();
                    if (countryUpperCase.equals("CN") || countryUpperCase.equals("DUANG")) {
                        toLang = provider == PROVIDER_MICROSOFT ? "zh-Hans" : "zh-CN";
                    } else if (countryUpperCase.equals("TW") || countryUpperCase.equals("HK")) {
                        toLang = provider == PROVIDER_MICROSOFT ? "zh-HanT" : "zh-TW";
                    } else {
                        toLang = language;
                    }
                } else {
                    toLang = language;
                }
                break;
        }
        return toLang;
    }

    abstract protected String translate(String query, String tl) throws IOException, JSONException;

    abstract protected List<String> getTargetLanguages();

    public interface TranslateCallBack {
        void onSuccess(Object translation);

        void onError(Throwable e);

        void onUnsupported();
    }

    @SuppressLint("StaticFieldLeak")
    private class MyAsyncTask extends AsyncTask<Void, Integer, Object> {
        TranslateCallBack translateCallBack;
        Object query;
        String tl;

        public MyAsyncTask request(Object query, String tl, TranslateCallBack translateCallBack) {
            this.query = query;
            this.tl = tl;
            this.translateCallBack = translateCallBack;
            return this;
        }

        @Override
        protected Object doInBackground(Void... params) {
            try {
                if (query instanceof String) {
                    return translate((String) query, tl);
                } else if (query instanceof TLRPC.Poll) {
                    TLRPC.TL_poll poll = new TLRPC.TL_poll();
                    TLRPC.TL_poll original = (TLRPC.TL_poll) query;
                    poll.question = original.question +
                            "\n" +
                            "--------" +
                            "\n" + translate(original.question, tl);
                    for (int i = 0; i < original.answers.size(); i++) {
                        TLRPC.TL_pollAnswer answer = new TLRPC.TL_pollAnswer();
                        answer.text = original.answers.get(i).text + " | " + translate(original.answers.get(i).text, tl);
                        answer.option = original.answers.get(i).option;
                        poll.answers.add(answer);
                    }
                    poll.close_date = original.close_date;
                    poll.close_period = original.close_period;
                    poll.closed = original.closed;
                    poll.flags = original.flags;
                    poll.id = original.id;
                    poll.multiple_choice = original.multiple_choice;
                    poll.public_voters = original.public_voters;
                    poll.quiz = original.quiz;
                    return poll;
                } else {
                    throw new UnsupportedOperationException("Unsupported translation query");
                }
            } catch (Throwable e) {
                FileLog.e(e);
                return e;
            }
        }

        @Override
        protected void onPostExecute(Object result) {
            if (result == null) {
                translateCallBack.onError(null);
            } else if (result instanceof Throwable) {
                translateCallBack.onError((Throwable) result);
            } else {
                translateCallBack.onSuccess(result);
            }
        }

    }

}
