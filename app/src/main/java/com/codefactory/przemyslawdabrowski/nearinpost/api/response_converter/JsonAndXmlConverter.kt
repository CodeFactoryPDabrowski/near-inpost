package com.codefactory.przemyslawdabrowski.nearinpost.api.response_converter

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

/**
 * Converter returns proper converter depends on response type -> json/xml.
 */
class JsonAndXmlConverter {

    @kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
    annotation class Json

    @kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
    annotation class Xml

    /**
     * Returns proper converter depends on response type.
     */
    class QualifiedTypeConverterFactory(val jsonFactory: Converter.Factory, val xmlFactory: Converter.Factory) : Converter.Factory() {

        override fun responseBodyConverter(type: Type?, annotations: Array<out Annotation>?, retrofit: Retrofit?): Converter<ResponseBody, *>? {
            for (annotation in annotations!!) {
                if (annotation is Json) {
                    return jsonFactory.responseBodyConverter(type, annotations, retrofit)
                }
                if (annotation is Xml) {
                    return xmlFactory.responseBodyConverter(type, annotations, retrofit)
                }
            }
            return null
        }

        override fun requestBodyConverter(type: Type?, parameterAnnotations: Array<out Annotation>?, methodAnnotations: Array<out Annotation>?, retrofit: Retrofit?): Converter<*, RequestBody>? {
            for (annotation in parameterAnnotations!!) {
                if (annotation is Json) {
                    return jsonFactory.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit)
                }
                if (annotation is Xml) {
                    return xmlFactory.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit)
                }
            }
            return null
        }
    }
}