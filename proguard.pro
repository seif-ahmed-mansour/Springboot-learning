# ======================


-libraryjars <java.home>/jmods/java.base.jmod(!**.jar;!module-info.class)
-libraryjars <java.home>/jmods/java.logging.jmod(!**.jar;!module-info.class)
-libraryjars <java.home>/jmods/java.sql.jmod(!**.jar;!module-info.class)
-libraryjars <java.home>/jmods/java.xml.jmod(!**.jar;!module-info.class)
-libraryjars <java.home>/jmods/java.desktop.jmod(!**.jar;!module-info.class)


# ======================
# Basic behavior
# ======================
-dontoptimize #disable byte code optimization to not break spring behavior(reflection)
-dontpreverify #skip byte code verification for simplicity
-dontskipnonpubliclibraryclasses #ensure even package private classes are analyzed
-allowaccessmodification
-keepattributes *Annotation*, Signature, InnerClasses, EnclosingMethod, RuntimeVisibleAnnotations, RuntimeInvisibleAnnotations
-printmapping mapping.txt

# -dontobfuscate use it untill finish shrinking and i will comment it later✅✅

# ======================
# Keep main entrypoints
# ======================
-keep public class * {
    public static void main(java.lang.String[]);
}

# ======================
# Spring Boot entry point
# ======================
-keep class org.springframework.boot.** { *; }
-keep class org.springframework.context.** { *; }
-keep class org.springframework.beans.** { *; }
-keep class org.springframework.core.** { *; }

# Keep annotations and stereotypes
-keep @org.springframework.stereotype.Controller class *
-keep @org.springframework.web.bind.annotation.RestController class *
-keep @org.springframework.stereotype.Service class *
-keep @org.springframework.stereotype.Repository class *
-keep @org.springframework.context.annotation.Configuration class *
-keepclassmembers class * {
    @org.springframework.beans.factory.annotation.Autowired *;
    @javax.annotation.Resource *;
}
-keep class * {
    @org.springframework.context.annotation.Bean *;
}

# ======================
# Jackson
# ======================
-keepclassmembers class * {
    @com.fasterxml.jackson.annotation.JsonCreator *;
    @com.fasterxml.jackson.annotation.JsonProperty *;
}
-keep class com.fasterxml.jackson.** { *; }
-dontwarn com.fasterxml.jackson.**

# ======================
# Lombok
# ======================
-dontwarn lombok.**
-keep class lombok.** { *; }

# ======================
# JPA / Hibernate entities
# ======================
-keep @jakarta.persistence.Entity class *
-keepclassmembers class * {
    @jakarta.persistence.Id *;
    @jakarta.persistence.EmbeddedId *;
}
-dontwarn jakarta.persistence.**
-dontwarn org.hibernate.**

# ======================
# OkHttp / Jackson / HTTP Clients
# ======================
-dontwarn okhttp3.**
-keep class okhttp3.** { *; }
-dontwarn org.springframework.http.**

# ======================
# Spring internals / proxies
# ======================
-keep class org.springframework.cglib.** { *; }
-keep class org.springframework.aop.framework.** { *; }
-dontwarn org.springframework.cglib.**

# ======================
# Resources / Directories
# ======================
-keepdirectories META-INF/
-keepdirectories templates/
-keepdirectories static/
-keepdirectories config/
