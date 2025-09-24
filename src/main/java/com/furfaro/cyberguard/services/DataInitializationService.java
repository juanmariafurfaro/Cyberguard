package com.furfaro.cyberguard.services;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.furfaro.cyberguard.dto.ConversationMessageDto;
import com.furfaro.cyberguard.dto.ProfileDto;
import com.furfaro.cyberguard.models.*;
import com.furfaro.cyberguard.models.Module;
import com.furfaro.cyberguard.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataInitializationService implements CommandLineRunner {

    private final CourseRepository courseRepository;
    private final ModuleRepository moduleRepository;
    private final ModuleContentRepository moduleContentRepository;
    private final QuestionRepository questionRepository;
    private final QuestionOptionRepository questionOptionRepository;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (courseRepository.count() == 0) {
            log.info("Inicializando datos de prueba...");
            initializeTestData();
            log.info("Datos de prueba inicializados correctamente");
        } else {
            log.info("Los datos ya existen, saltando inicialización");
        }
    }

    private void initializeTestData() throws Exception {
        // Crear cursos
        Course phishingCourse = createPhishingCourse();
        Course pretextingCourse = createPretextingCourse();
        
        courseRepository.saveAll(Arrays.asList(phishingCourse, pretextingCourse));
        
        // Crear módulos y contenido
        createPhishingModules(phishingCourse);
        createPretextingModules(pretextingCourse);
    }

    private Course createPhishingCourse() {
        Course course = new Course();
        course.setTitle("Introducción al Phishing");
        course.setDescription("Aprende a identificar y evitar ataques de phishing mediante emails fraudulentos. Este curso cubre desde conceptos básicos hasta técnicas avanzadas de detección.");
        course.setCourseType(CourseType.PHISHING);
        course.setPassingScore(70);
        course.setIsActive(true);
        return course;
    }

    private Course createPretextingCourse() {
        Course course = new Course();
        course.setTitle("Detección de Pretexting");
        course.setDescription("Domina las técnicas para reconocer intentos de pretexting y manipulación social. Aprende a identificar conversaciones sospechosas y protegerte.");
        course.setCourseType(CourseType.PRETEXTING);
        course.setPassingScore(75);
        course.setIsActive(true);
        return course;
    }

    private void createPhishingModules(Course course) throws Exception {
        // Módulo 1: Teoría Inicial
        Module intro = createModule("Introducción al Phishing", 
                "Conceptos fundamentales sobre qué es el phishing, cómo funciona y por qué es peligroso.",
                ModuleType.TEORIA_INICIAL, 0, course);
        
        // Módulo 2: Actividad Práctica
        Module practice = createModule("Simulación: Identificar Emails",
                "Práctica interactiva para identificar emails de phishing reales vs legítimos.",
                ModuleType.ACTIVIDAD_PRACTICA, 1, course);
        
        // Módulo 3: Teoría Adicional
        Module advanced = createModule("Técnicas Avanzadas de Phishing",
                "Aprende sobre spear phishing, whaling y otras técnicas sofisticadas.",
                ModuleType.TEORIA_ADICIONAL, 2, course);
        
        // Módulo 4: Otra Actividad
        Module cases = createModule("Casos Reales de Phishing",
                "Análisis de ataques de phishing famosos y sus consecuencias.",
                ModuleType.OTRA_ACTIVIDAD, 3, course);
        
        // Módulo 5: Teoría Final
        Module protection = createModule("Medidas de Protección",
                "Estrategias y herramientas para protegerse contra el phishing.",
                ModuleType.TEORIA_FINAL, 4, course);
        
        // Módulo 6: Cuestionario Final
        Module quiz = createModule("Evaluación Final - Phishing",
                "Cuestionario final para evaluar tu comprensión sobre phishing.",
                ModuleType.CUESTIONARIO_FINAL, 5, course);

        moduleRepository.saveAll(Arrays.asList(intro, practice, advanced, cases, protection, quiz));

        // Crear contenido para módulos
        createPhishingContent(intro, practice, quiz);
    }

    private void createPretextingModules(Course course) throws Exception {
        Module intro = createModule("¿Qué es el Pretexting?",
                "Fundamentos del pretexting como técnica de ingeniería social.",
                ModuleType.TEORIA_INICIAL, 0, course);
        
        Module practice = createModule("Simulación: Conversaciones Sospechosas",
                "Práctica para identificar intentos de pretexting en conversaciones.",
                ModuleType.ACTIVIDAD_PRACTICA, 1, course);
        
        Module advanced = createModule("Perfiles y Escenarios",
                "Cómo los atacantes crean perfiles falsos y escenarios convincentes.",
                ModuleType.TEORIA_ADICIONAL, 2, course);
        
        Module cases = createModule("Análisis de Casos",
                "Estudia casos reales de pretexting y sus métodos.",
                ModuleType.OTRA_ACTIVIDAD, 3, course);
        
        Module protection = createModule("Defensas contra Pretexting",
                "Estrategias para identificar y defenderse del pretexting.",
                ModuleType.TEORIA_FINAL, 4, course);
        
        Module quiz = createModule("Evaluación Final - Pretexting",
                "Cuestionario final para evaluar tu conocimiento sobre pretexting.",
                ModuleType.CUESTIONARIO_FINAL, 5, course);

        moduleRepository.saveAll(Arrays.asList(intro, practice, advanced, cases, protection, quiz));

        // Crear contenido para módulos
        createPretextingContent(intro, practice, quiz);
    }

    private Module createModule(String title, String description, ModuleType type, int order, Course course) {
        Module module = new Module();
        module.setTitle(title);
        module.setDescription(description);
        module.setModuleType(type);
        module.setOrderIndex(order);
        module.setIsRequired(true);
        module.setCourse(course);
        return module;
    }

    private void createPhishingContent(Module introModule, Module practiceModule, Module quizModule) throws Exception {
        // Contenido teórico para introducción
        ModuleContent content1 = new ModuleContent();
        content1.setTitle("¿Qué es el Phishing?");
        content1.setContentType(ContentType.TEXT);
        content1.setTextContent("El phishing es una técnica de ingeniería social que utiliza comunicaciones electrónicas fraudulentas para engañar a las víctimas y obtener información confidencial como contraseñas, números de tarjetas de crédito o datos personales.\n\n## Características principales:\n- **Suplantación de identidad**: Los atacantes se hacen pasar por entidades confiables\n- **Urgencia artificial**: Crean sensación de urgencia para presionar a la víctima\n- **Enlaces maliciosos**: Redirigen a sitios web falsos que imitan servicios legítimos\n- **Técnicas psicológicas**: Explotan emociones como miedo, curiosidad o codicia");
        content1.setOrderIndex(0);
        content1.setModule(introModule);

        ModuleContent content2 = new ModuleContent();
        content2.setTitle("Tipos de Phishing");
        content2.setContentType(ContentType.TEXT);
        content2.setTextContent("## Principales tipos de phishing:\n\n### 1. Email Phishing\nEl más común, utiliza correos masivos suplantando organizaciones conocidas.\n\n### 2. Spear Phishing\nAtaques dirigidos a individuos específicos con información personalizada.\n\n### 3. Whaling\nDirigido a ejecutivos y personas de alto perfil en organizaciones.\n\n### 4. Vishing (Voice Phishing)\nUtiliza llamadas telefónicas para obtener información.\n\n### 5. Smishing (SMS Phishing)\nA través de mensajes de texto SMS.");
        content2.setOrderIndex(1);
        content2.setModule(introModule);

        moduleContentRepository.saveAll(Arrays.asList(content1, content2));

        // Preguntas de phishing para la práctica
        createPhishingQuestions(practiceModule);
        
        // Preguntas para el quiz final
        createPhishingQuizQuestions(quizModule);
    }

    private void createPretextingContent(Module introModule, Module practiceModule, Module quizModule) throws Exception {
        // Contenido teórico
        ModuleContent content1 = new ModuleContent();
        content1.setTitle("Definición de Pretexting");
        content1.setContentType(ContentType.TEXT);
        content1.setTextContent("El pretexting es una técnica de ingeniería social donde el atacante crea un escenario fabricado (pretexto) para interactuar con la víctima y obtener información confidencial.\n\n## Elementos clave:\n- **Investigación previa**: El atacante recopila información sobre la víctima\n- **Creación de personaje**: Asume una identidad falsa creíble\n- **Escenario convincente**: Construye una situación que justifique su solicitud\n- **Explotación de confianza**: Aprovecha la tendencia natural a ayudar");
        content1.setOrderIndex(0);
        content1.setModule(introModule);

        moduleContentRepository.save(content1);

        // Preguntas de pretexting para la práctica
        createPretextingQuestions(practiceModule);
        
        // Preguntas para el quiz final
        createPretextingQuizQuestions(quizModule);
    }

    private void createPhishingQuestions(Module module) throws Exception {
        // Pregunta 1: Email de phishing típico
        Question q1 = new Question();
        q1.setQuestionText("Analiza este email y determina si es phishing o legítimo:");
        q1.setQuestionType(QuestionType.PHISHING_EMAIL);
        q1.setOrderIndex(0);
        q1.setPoints(10);
        q1.setModule(module);
        q1.setSenderEmail("security@paypal-verificacion.net");
        q1.setEmailSubject("URGENTE: Verifique su cuenta PayPal");
        q1.setEmailContent("Estimado cliente,\n\nHemos detectado actividad sospechosa en su cuenta de PayPal. Para proteger sus fondos, debe verificar su identidad inmediatamente.\n\nHaga clic aquí para verificar: http://paypal-verificacion.net/verify\n\nSi no verifica en las próximas 24 horas, su cuenta será suspendida permanentemente.\n\nAtentamente,\nEquipo de Seguridad PayPal");
        q1.setExplanation("Este es un email de phishing típico. Indicadores: URL sospechosa (paypal-verificacion.net en lugar de paypal.com), presión temporal, amenaza de suspensión, y solicitud de verificación por email.");
        
        questionRepository.save(q1);

        // Opciones para q1
        QuestionOption q1o1 = new QuestionOption();
        q1o1.setOptionText("Es phishing");
        q1o1.setIsCorrect(true);
        q1o1.setOrderIndex(0);
        q1o1.setQuestion(q1);

        QuestionOption q1o2 = new QuestionOption();
        q1o2.setOptionText("Es legítimo");
        q1o2.setIsCorrect(false);
        q1o2.setOrderIndex(1);
        q1o2.setQuestion(q1);

        questionOptionRepository.saveAll(Arrays.asList(q1o1, q1o2));

        // Pregunta 2: Email legítimo
        Question q2 = new Question();
        q2.setQuestionText("Evalúa este segundo email:");
        q2.setQuestionType(QuestionType.PHISHING_EMAIL);
        q2.setOrderIndex(1);
        q2.setPoints(10);
        q2.setModule(module);
        q2.setSenderEmail("delivery@amazon.com");
        q2.setEmailSubject("Tu paquete está esperando");
        q2.setEmailContent("Hola,\n\nTu paquete de Amazon está esperando en nuestro centro de distribución.\n\nPara coordinar la entrega, confirma tu dirección en: amazon.com/tracking\n\nCódigo de seguimiento: AMZ-789456123\n\nSaludos,\nCentro de Distribución Amazon");
        q2.setExplanation("Este email es legítimo. La URL es correcta (amazon.com), no hay presión excesiva, el tono es profesional, y proporciona información útil (código de seguimiento).");
        
        questionRepository.save(q2);

        // Opciones para q2
        QuestionOption q2o1 = new QuestionOption();
        q2o1.setOptionText("Es phishing");
        q2o1.setIsCorrect(false);
        q2o1.setOrderIndex(0);
        q2o1.setQuestion(q2);

        QuestionOption q2o2 = new QuestionOption();
        q2o2.setOptionText("Es legítimo");
        q2o2.setIsCorrect(true);
        q2o2.setOrderIndex(1);
        q2o2.setQuestion(q2);

        questionOptionRepository.saveAll(Arrays.asList(q2o1, q2o2));
    }

    private void createPretextingQuestions(Module module) throws Exception {
        // Crear perfil de prueba
        ProfileDto profile = ProfileDto.builder()
                .name("María García")
                .company("TechCorp SA")
                .position("Analista de Sistemas")
                .email("maria.garcia@techcorp.com")
                .phone("+54 11 4567-8900")
                .location("Buenos Aires, Argentina")
                .build();

        // Crear conversación de pretexting
        List<ConversationMessageDto> conversation = Arrays.asList(
            ConversationMessageDto.builder()
                .sender("Caller")
                .message("Hola María, soy Juan del departamento de IT corporativo")
                .timestamp("14:30")
                .isFromTarget(false)
                .build(),
            ConversationMessageDto.builder()
                .sender("María")
                .message("Hola Juan, no te conozco del departamento de IT")
                .timestamp("14:31")
                .isFromTarget(true)
                .build(),
            ConversationMessageDto.builder()
                .sender("Caller")
                .message("Soy nuevo, empecé la semana pasada. Estoy actualizando los sistemas de seguridad y necesito verificar tu contraseña del sistema")
                .timestamp("14:32")
                .isFromTarget(false)
                .build(),
            ConversationMessageDto.builder()
                .sender("María")
                .message("¿No deberías tener acceso a esa información desde tu sistema?")
                .timestamp("14:33")
                .isFromTarget(true)
                .build(),
            ConversationMessageDto.builder()
                .sender("Caller")
                .message("Normalmente sí, pero hay un problema con nuestro servidor. Es urgente, el CEO quiere que terminemos hoy")
                .timestamp("14:34")
                .isFromTarget(false)
                .build()
        );

        Question q1 = new Question();
        q1.setQuestionText("Analiza esta conversación y determina si es pretexting:");
        q1.setQuestionType(QuestionType.PRETEXTING_CONVERSATION);
        q1.setOrderIndex(0);
        q1.setPoints(10);
        q1.setModule(module);
        q1.setProfileData(objectMapper.writeValueAsString(profile));
        q1.setConversationData(objectMapper.writeValueAsString(conversation));
        q1.setExplanation("Este es claramente pretexting. El atacante: 1) Se presenta como empleado nuevo para justificar que no lo conozca, 2) Solicita contraseñas directamente, 3) Crea presión artificial mencionando al CEO, 4) Inventa excusas técnicas para justificar su solicitud inusual.");
        
        questionRepository.save(q1);

        // Opciones para la pregunta de pretexting
        QuestionOption q1o1 = new QuestionOption();
        q1o1.setOptionText("Es pretexting");
        q1o1.setIsCorrect(true);
        q1o1.setOrderIndex(0);
        q1o1.setQuestion(q1);

        QuestionOption q1o2 = new QuestionOption();
        q1o2.setOptionText("Es conversación normal");
        q1o2.setIsCorrect(false);
        q1o2.setOrderIndex(1);
        q1o2.setQuestion(q1);

        questionOptionRepository.saveAll(Arrays.asList(q1o1, q1o2));
    }

    private void createPhishingQuizQuestions(Module module) {
        // Pregunta 1 del quiz
        Question q1 = new Question();
        q1.setQuestionText("¿Cuál es el principal objetivo del phishing?");
        q1.setQuestionType(QuestionType.MULTIPLE_CHOICE);
        q1.setOrderIndex(0);
        q1.setPoints(5);
        q1.setModule(module);
        q1.setExplanation("El phishing busca principalmente obtener información confidencial de las víctimas, como credenciales, datos bancarios o información personal.");
        questionRepository.save(q1);

        // Opciones para q1
        QuestionOption q1o1 = new QuestionOption();
        q1o1.setOptionText("Vender productos");
        q1o1.setIsCorrect(false);
        q1o1.setOrderIndex(0);
        q1o1.setQuestion(q1);

        QuestionOption q1o2 = new QuestionOption();
        q1o2.setOptionText("Obtener información confidencial");
        q1o2.setIsCorrect(true);
        q1o2.setOrderIndex(1);
        q1o2.setQuestion(q1);

        QuestionOption q1o3 = new QuestionOption();
        q1o3.setOptionText("Hacer publicidad");
        q1o3.setIsCorrect(false);
        q1o3.setOrderIndex(2);
        q1o3.setQuestion(q1);

        QuestionOption q1o4 = new QuestionOption();
        q1o4.setOptionText("Entretener a las personas");
        q1o4.setIsCorrect(false);
        q1o4.setOrderIndex(3);
        q1o4.setQuestion(q1);

        questionOptionRepository.saveAll(Arrays.asList(q1o1, q1o2, q1o3, q1o4));

        // Pregunta 2 del quiz
        Question q2 = new Question();
        q2.setQuestionText("¿Qué debes hacer si recibes un email sospechoso?");
        q2.setQuestionType(QuestionType.MULTIPLE_CHOICE);
        q2.setOrderIndex(1);
        q2.setPoints(5);
        q2.setModule(module);
        q2.setExplanation("Nunca hagas clic en enlaces ni descargues archivos de emails sospechosos. Lo correcto es reportar y eliminar.");
        questionRepository.save(q2);

        // Opciones para q2
        QuestionOption q2o1 = new QuestionOption();
        q2o1.setOptionText("Hacer clic inmediatamente");
        q2o1.setIsCorrect(false);
        q2o1.setOrderIndex(0);
        q2o1.setQuestion(q2);

        QuestionOption q2o2 = new QuestionOption();
        q2o2.setOptionText("Responder con tus datos");
        q2o2.setIsCorrect(false);
        q2o2.setOrderIndex(1);
        q2o2.setQuestion(q2);

        QuestionOption q2o3 = new QuestionOption();
        q2o3.setOptionText("Reportar como spam y eliminar");
        q2o3.setIsCorrect(true);
        q2o3.setOrderIndex(2);
        q2o3.setQuestion(q2);

        QuestionOption q2o4 = new QuestionOption();
        q2o4.setOptionText("Reenviar a tus contactos");
        q2o4.setIsCorrect(false);
        q2o4.setOrderIndex(3);
        q2o4.setQuestion(q2);

        questionOptionRepository.saveAll(Arrays.asList(q2o1, q2o2, q2o3, q2o4));
    }

    private void createPretextingQuizQuestions(Module module) {
        // Pregunta 1 del quiz de pretexting
        Question q1 = new Question();
        q1.setQuestionText("¿Qué es lo más importante para identificar pretexting?");
        q1.setQuestionType(QuestionType.MULTIPLE_CHOICE);
        q1.setOrderIndex(0);
        q1.setPoints(5);
        q1.setModule(module);
        q1.setExplanation("La clave está en verificar la identidad del solicitante a través de canales oficiales independientes.");
        questionRepository.save(q1);

        // Opciones para q1
        QuestionOption q1o1 = new QuestionOption();
        q1o1.setOptionText("La velocidad de la conversación");
        q1o1.setIsCorrect(false);
        q1o1.setOrderIndex(0);
        q1o1.setQuestion(q1);

        QuestionOption q1o2 = new QuestionOption();
        q1o2.setOptionText("El acento del hablante");
        q1o2.setIsCorrect(false);
        q1o2.setOrderIndex(1);
        q1o2.setQuestion(q1);

        QuestionOption q1o3 = new QuestionOption();
        q1o3.setOptionText("Verificar la identidad del solicitante");
        q1o3.setIsCorrect(true);
        q1o3.setOrderIndex(2);
        q1o3.setQuestion(q1);

        QuestionOption q1o4 = new QuestionOption();
        q1o4.setOptionText("La hora de la llamada");
        q1o4.setIsCorrect(false);
        q1o4.setOrderIndex(3);
        q1o4.setQuestion(q1);

        questionOptionRepository.saveAll(Arrays.asList(q1o1, q1o2, q1o3, q1o4));
    }
}