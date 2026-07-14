--
-- PostgreSQL database dump
--

\restrict X7GXhsO1w2bqonNXFZrw7RjMM8YMTIkRMontT7D1vD3YzCX4u5pjOofX8iULSxE

-- Dumped from database version 17.10 (986efc8)
-- Dumped by pg_dump version 17.6

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: application_status; Type: TYPE; Schema: public; Owner: neondb_owner
--

CREATE TYPE public.application_status AS ENUM (
    'PENDING',
    'ACCEPTED',
    'REJECTED'
);


ALTER TYPE public.application_status OWNER TO neondb_owner;

--
-- Name: audit_action; Type: TYPE; Schema: public; Owner: neondb_owner
--

CREATE TYPE public.audit_action AS ENUM (
    'INSERT',
    'UPDATE',
    'DELETE'
);


ALTER TYPE public.audit_action OWNER TO neondb_owner;

--
-- Name: budget_type; Type: TYPE; Schema: public; Owner: neondb_owner
--

CREATE TYPE public.budget_type AS ENUM (
    'FIJO',
    'POR_HORA'
);


ALTER TYPE public.budget_type OWNER TO neondb_owner;

--
-- Name: budgettype; Type: TYPE; Schema: public; Owner: neondb_owner
--

CREATE TYPE public.budgettype AS ENUM (
    'FIJO',
    'POR_HORA'
);


ALTER TYPE public.budgettype OWNER TO neondb_owner;

--
-- Name: contract_status; Type: TYPE; Schema: public; Owner: neondb_owner
--

CREATE TYPE public.contract_status AS ENUM (
    'DRAFT',
    'SIGNED',
    'COMPLETED'
);


ALTER TYPE public.contract_status OWNER TO neondb_owner;

--
-- Name: dispute_status; Type: TYPE; Schema: public; Owner: neondb_owner
--

CREATE TYPE public.dispute_status AS ENUM (
    'PENDING_AI_REVIEW',
    'WAITING_PARTIES',
    'ESCALATED_TO_ADMIN',
    'RESOLVED'
);


ALTER TYPE public.dispute_status OWNER TO neondb_owner;

--
-- Name: evaluation_phase_type; Type: TYPE; Schema: public; Owner: neondb_owner
--

CREATE TYPE public.evaluation_phase_type AS ENUM (
    'VOICE_INTERVIEW',
    'PRACTICAL_SANDBOX',
    'CODE_DEFENSE'
);


ALTER TYPE public.evaluation_phase_type OWNER TO neondb_owner;

--
-- Name: fraud_event_type; Type: TYPE; Schema: public; Owner: neondb_owner
--

CREATE TYPE public.fraud_event_type AS ENUM (
    'TAB_SWITCH',
    'COPY_PASTE',
    'MOUSE_OUT_OF_BOUNDS',
    'AUDIO_ANOMALY',
    'NO_FACE_DETECTED'
);


ALTER TYPE public.fraud_event_type OWNER TO neondb_owner;

--
-- Name: milestone_status; Type: TYPE; Schema: public; Owner: neondb_owner
--

CREATE TYPE public.milestone_status AS ENUM (
    'PENDING_FUNDING',
    'FUNDED',
    'DELIVERED',
    'APPROVED',
    'REJECTED',
    'DISPUTED',
    'PAID'
);


ALTER TYPE public.milestone_status OWNER TO neondb_owner;

--
-- Name: milestonestatus; Type: TYPE; Schema: public; Owner: neondb_owner
--

CREATE TYPE public.milestonestatus AS ENUM (
    'APROBADO',
    'DELIVERED',
    'DISPUTED',
    'ENTREGADO',
    'EN_DISPUTA',
    'FONDEADO',
    'FUNDED',
    'PAGADO',
    'PAID',
    'PENDIENTE_FINANCIAMIENTO',
    'PENDING_FUNDING',
    'REJECTED'
);


ALTER TYPE public.milestonestatus OWNER TO neondb_owner;

--
-- Name: modality_type; Type: TYPE; Schema: public; Owner: neondb_owner
--

CREATE TYPE public.modality_type AS ENUM (
    'REMOTO',
    'PRESENCIAL',
    'HIBRIDO'
);


ALTER TYPE public.modality_type OWNER TO neondb_owner;

--
-- Name: modalitytype; Type: TYPE; Schema: public; Owner: neondb_owner
--

CREATE TYPE public.modalitytype AS ENUM (
    'HIBRIDO',
    'PRESENCIAL',
    'REMOTO'
);


ALTER TYPE public.modalitytype OWNER TO neondb_owner;

--
-- Name: offer_milestone_status; Type: TYPE; Schema: public; Owner: neondb_owner
--

CREATE TYPE public.offer_milestone_status AS ENUM (
    'PENDIENTE_FINANCIAMIENTO',
    'FONDEADO',
    'ENTREGADO',
    'APROBADO',
    'EN_DISPUTA',
    'PAGADO'
);


ALTER TYPE public.offer_milestone_status OWNER TO neondb_owner;

--
-- Name: offer_status; Type: TYPE; Schema: public; Owner: neondb_owner
--

CREATE TYPE public.offer_status AS ENUM (
    'ABIERTA',
    'EN_PROCESO',
    'COMPLETADA',
    'CANCELADA'
);


ALTER TYPE public.offer_status OWNER TO neondb_owner;

--
-- Name: offerstatus; Type: TYPE; Schema: public; Owner: neondb_owner
--

CREATE TYPE public.offerstatus AS ENUM (
    'ABIERTA',
    'CANCELADA',
    'COMPLETADA',
    'EN_PROCESO'
);


ALTER TYPE public.offerstatus OWNER TO neondb_owner;

--
-- Name: payment_method; Type: TYPE; Schema: public; Owner: neondb_owner
--

CREATE TYPE public.payment_method AS ENUM (
    'CREDIT_CARD',
    'PAYPAL',
    'BANK_TRANSFER',
    'PLATFORM_BALANCE'
);


ALTER TYPE public.payment_method OWNER TO neondb_owner;

--
-- Name: payment_status; Type: TYPE; Schema: public; Owner: neondb_owner
--

CREATE TYPE public.payment_status AS ENUM (
    'PENDING',
    'COMPLETED',
    'FAILED',
    'REFUNDED'
);


ALTER TYPE public.payment_status OWNER TO neondb_owner;

--
-- Name: payment_type; Type: TYPE; Schema: public; Owner: neondb_owner
--

CREATE TYPE public.payment_type AS ENUM (
    'ESCROW_FUNDING',
    'FREELANCER_PAYOUT',
    'REFUND'
);


ALTER TYPE public.payment_type OWNER TO neondb_owner;

--
-- Name: permisos_name; Type: TYPE; Schema: public; Owner: neondb_owner
--

CREATE TYPE public.permisos_name AS ENUM (
    'EDITAR_PERFIL'
);


ALTER TYPE public.permisos_name OWNER TO neondb_owner;

--
-- Name: project_category; Type: TYPE; Schema: public; Owner: neondb_owner
--

CREATE TYPE public.project_category AS ENUM (
    'DESARROLLO_WEB',
    'DESARROLLO_MOVIL',
    'DISENO_GRAFICO',
    'MARKETING_DIGITAL',
    'CONTABILIDAD',
    'CONSULTORIA',
    'TRADUCCION',
    'REDACCION',
    'OTROS'
);


ALTER TYPE public.project_category OWNER TO neondb_owner;

--
-- Name: projectcategory; Type: TYPE; Schema: public; Owner: neondb_owner
--

CREATE TYPE public.projectcategory AS ENUM (
    'CONSULTORIA',
    'CONTABILIDAD',
    'DESARROLLO_MOVIL',
    'DESARROLLO_WEB',
    'DISENO_GRAFICO',
    'MARKETING_DIGITAL',
    'OTROS',
    'REDACCION',
    'TRADUCCION'
);


ALTER TYPE public.projectcategory OWNER TO neondb_owner;

--
-- Name: role_name; Type: TYPE; Schema: public; Owner: neondb_owner
--

CREATE TYPE public.role_name AS ENUM (
    'FREELANCER',
    'PYME',
    'ADMIN'
);


ALTER TYPE public.role_name OWNER TO neondb_owner;

--
-- Name: session_status; Type: TYPE; Schema: public; Owner: neondb_owner
--

CREATE TYPE public.session_status AS ENUM (
    'IN_PROGRESS',
    'COMPLETED',
    'FAILED',
    'FLAGGED_FOR_FRAUD'
);


ALTER TYPE public.session_status OWNER TO neondb_owner;

--
-- Name: CAST (public.budgettype AS character varying); Type: CAST; Schema: -; Owner: -
--

CREATE CAST (public.budgettype AS character varying) WITH INOUT AS IMPLICIT;


--
-- Name: CAST (public.milestonestatus AS character varying); Type: CAST; Schema: -; Owner: -
--

CREATE CAST (public.milestonestatus AS character varying) WITH INOUT AS IMPLICIT;


--
-- Name: CAST (public.modalitytype AS character varying); Type: CAST; Schema: -; Owner: -
--

CREATE CAST (public.modalitytype AS character varying) WITH INOUT AS IMPLICIT;


--
-- Name: CAST (public.offerstatus AS character varying); Type: CAST; Schema: -; Owner: -
--

CREATE CAST (public.offerstatus AS character varying) WITH INOUT AS IMPLICIT;


--
-- Name: CAST (public.projectcategory AS character varying); Type: CAST; Schema: -; Owner: -
--

CREATE CAST (public.projectcategory AS character varying) WITH INOUT AS IMPLICIT;


--
-- Name: CAST (character varying AS public.budgettype); Type: CAST; Schema: -; Owner: -
--

CREATE CAST (character varying AS public.budgettype) WITH INOUT AS IMPLICIT;


--
-- Name: CAST (character varying AS public.milestonestatus); Type: CAST; Schema: -; Owner: -
--

CREATE CAST (character varying AS public.milestonestatus) WITH INOUT AS IMPLICIT;


--
-- Name: CAST (character varying AS public.modalitytype); Type: CAST; Schema: -; Owner: -
--

CREATE CAST (character varying AS public.modalitytype) WITH INOUT AS IMPLICIT;


--
-- Name: CAST (character varying AS public.offerstatus); Type: CAST; Schema: -; Owner: -
--

CREATE CAST (character varying AS public.offerstatus) WITH INOUT AS IMPLICIT;


--
-- Name: CAST (character varying AS public.projectcategory); Type: CAST; Schema: -; Owner: -
--

CREATE CAST (character varying AS public.projectcategory) WITH INOUT AS IMPLICIT;


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: applications; Type: TABLE; Schema: public; Owner: neondb_owner
--

CREATE TABLE public.applications (
    id uuid NOT NULL,
    estimated_days integer,
    proposed_amount double precision,
    status character varying(255),
    freelancer_id uuid,
    offer_id uuid,
    CONSTRAINT applications_status_check CHECK (((status)::text = ANY ((ARRAY['PENDING'::character varying, 'ACCEPTED'::character varying, 'REJECTED'::character varying])::text[])))
);


ALTER TABLE public.applications OWNER TO neondb_owner;

--
-- Name: assessments; Type: TABLE; Schema: public; Owner: neondb_owner
--

CREATE TABLE public.assessments (
    id uuid NOT NULL,
    passed boolean,
    quiz_data jsonb,
    score double precision,
    freelancer_id uuid,
    skill_id integer
);


ALTER TABLE public.assessments OWNER TO neondb_owner;

--
-- Name: audit_logs; Type: TABLE; Schema: public; Owner: neondb_owner
--

CREATE TABLE public.audit_logs (
    id uuid NOT NULL,
    action character varying(255),
    changed_by_user_id uuid,
    ip_address character varying(255),
    new_data jsonb,
    old_data jsonb,
    record_id uuid,
    table_name character varying(255),
    "timestamp" timestamp(6) without time zone,
    CONSTRAINT audit_logs_action_check CHECK (((action)::text = ANY ((ARRAY['INSERT'::character varying, 'UPDATE'::character varying, 'DELETE'::character varying])::text[])))
);


ALTER TABLE public.audit_logs OWNER TO neondb_owner;

--
-- Name: contracts; Type: TABLE; Schema: public; Owner: neondb_owner
--

CREATE TABLE public.contracts (
    id uuid NOT NULL,
    digital_signature_freelancer text,
    digital_signature_pyme text,
    status character varying(255),
    application_id uuid,
    CONSTRAINT contracts_status_check CHECK (((status)::text = ANY ((ARRAY['DRAFT'::character varying, 'SIGNED'::character varying, 'COMPLETED'::character varying])::text[])))
);


ALTER TABLE public.contracts OWNER TO neondb_owner;

--
-- Name: deliverables; Type: TABLE; Schema: public; Owner: neondb_owner
--

CREATE TABLE public.deliverables (
    id uuid NOT NULL,
    evidence_url character varying(255),
    notes text,
    submitted_at timestamp(6) without time zone,
    milestone_id uuid
);


ALTER TABLE public.deliverables OWNER TO neondb_owner;

--
-- Name: disputes; Type: TABLE; Schema: public; Owner: neondb_owner
--

CREATE TABLE public.disputes (
    id uuid NOT NULL,
    ai_resolution_report jsonb,
    status character varying(255),
    milestone_id uuid,
    CONSTRAINT disputes_status_check CHECK (((status)::text = ANY ((ARRAY['PENDING_AI_REVIEW'::character varying, 'WAITING_PARTIES'::character varying, 'ESCALATED_TO_ADMIN'::character varying, 'RESOLVED'::character varying])::text[])))
);


ALTER TABLE public.disputes OWNER TO neondb_owner;

--
-- Name: evaluation_phase_templates; Type: TABLE; Schema: public; Owner: neondb_owner
--

CREATE TABLE public.evaluation_phase_templates (
    id uuid NOT NULL,
    instructions_prompt text NOT NULL,
    linked_silabo_ids integer[],
    order_index integer NOT NULL,
    passing_score double precision NOT NULL,
    phase_type public.evaluation_phase_type NOT NULL,
    time_limit_minutes integer NOT NULL,
    title character varying(255) NOT NULL,
    specialty_id integer NOT NULL,
    CONSTRAINT evaluation_phase_templates_phase_type_check CHECK ((phase_type = ANY (ARRAY['VOICE_INTERVIEW'::public.evaluation_phase_type, 'PRACTICAL_SANDBOX'::public.evaluation_phase_type, 'CODE_DEFENSE'::public.evaluation_phase_type])))
);


ALTER TABLE public.evaluation_phase_templates OWNER TO neondb_owner;

--
-- Name: evaluation_phases; Type: TABLE; Schema: public; Owner: neondb_owner
--

CREATE TABLE public.evaluation_phases (
    id uuid NOT NULL,
    ai_feedback jsonb,
    ended_at timestamp(6) without time zone,
    phase_score double precision,
    phase_type public.evaluation_phase_type NOT NULL,
    plagiarism_summary jsonb,
    started_at timestamp(6) without time zone,
    status character varying(20),
    submitted_at timestamp(6) without time zone,
    time_spent_seconds integer,
    user_response_data jsonb,
    session_id uuid NOT NULL,
    CONSTRAINT evaluation_phases_phase_type_check CHECK ((phase_type = ANY (ARRAY['VOICE_INTERVIEW'::public.evaluation_phase_type, 'PRACTICAL_SANDBOX'::public.evaluation_phase_type, 'CODE_DEFENSE'::public.evaluation_phase_type])))
);


ALTER TABLE public.evaluation_phases OWNER TO neondb_owner;

--
-- Name: evaluation_sessions; Type: TABLE; Schema: public; Owner: neondb_owner
--

CREATE TABLE public.evaluation_sessions (
    id uuid NOT NULL,
    completed_at timestamp(6) without time zone,
    final_score double precision,
    is_passed boolean,
    started_at timestamp(6) without time zone,
    status public.session_status,
    freelancer_id uuid NOT NULL,
    freelancer_specialty_id uuid NOT NULL,
    CONSTRAINT evaluation_sessions_status_check CHECK ((status = ANY (ARRAY['IN_PROGRESS'::public.session_status, 'COMPLETED'::public.session_status, 'FAILED'::public.session_status, 'FLAGGED_FOR_FRAUD'::public.session_status])))
);


ALTER TABLE public.evaluation_sessions OWNER TO neondb_owner;

--
-- Name: fraud_telemetry_logs; Type: TABLE; Schema: public; Owner: neondb_owner
--

CREATE TABLE public.fraud_telemetry_logs (
    id uuid NOT NULL,
    event_type public.fraud_event_type NOT NULL,
    metadata jsonb,
    "timestamp" timestamp(6) without time zone,
    session_id uuid NOT NULL,
    CONSTRAINT fraud_telemetry_logs_event_type_check CHECK ((event_type = ANY (ARRAY['TAB_SWITCH'::public.fraud_event_type, 'COPY_PASTE'::public.fraud_event_type, 'MOUSE_OUT_OF_BOUNDS'::public.fraud_event_type, 'AUDIO_ANOMALY'::public.fraud_event_type, 'NO_FACE_DETECTED'::public.fraud_event_type])))
);


ALTER TABLE public.fraud_telemetry_logs OWNER TO neondb_owner;

--
-- Name: freelancer_profiles; Type: TABLE; Schema: public; Owner: neondb_owner
--

CREATE TABLE public.freelancer_profiles (
    user_id uuid NOT NULL,
    bio text,
    first_name character varying(255),
    last_name character varying(255)
);


ALTER TABLE public.freelancer_profiles OWNER TO neondb_owner;

--
-- Name: freelancer_skills; Type: TABLE; Schema: public; Owner: neondb_owner
--

CREATE TABLE public.freelancer_skills (
    freelancer_id uuid NOT NULL,
    skill_id integer NOT NULL
);


ALTER TABLE public.freelancer_skills OWNER TO neondb_owner;

--
-- Name: freelancer_specialties; Type: TABLE; Schema: public; Owner: neondb_owner
--

CREATE TABLE public.freelancer_specialties (
    id uuid NOT NULL,
    achieved_at timestamp(6) without time zone,
    enrolled_at timestamp(6) without time zone,
    freelancer_id uuid NOT NULL,
    specialty_id integer NOT NULL
);


ALTER TABLE public.freelancer_specialties OWNER TO neondb_owner;

--
-- Name: job_offers; Type: TABLE; Schema: public; Owner: neondb_owner
--

CREATE TABLE public.job_offers (
    id uuid NOT NULL,
    budget_type public.budget_type,
    description text,
    status public.offer_status,
    title character varying(255),
    total_budget double precision,
    pyme_id uuid,
    estimated_days integer,
    location character varying(255),
    max_budget double precision,
    min_budget double precision,
    published_at date,
    modality public.modality_type,
    project_category public.project_category,
    specialty_id integer
);


ALTER TABLE public.job_offers OWNER TO neondb_owner;

--
-- Name: milestones; Type: TABLE; Schema: public; Owner: neondb_owner
--

CREATE TABLE public.milestones (
    id uuid NOT NULL,
    amount double precision,
    deadline date,
    status character varying(255),
    title character varying(255),
    contract_id uuid,
    CONSTRAINT milestones_status_check CHECK (((status)::text = ANY ((ARRAY['PENDING_FUNDING'::character varying, 'FUNDED'::character varying, 'DELIVERED'::character varying, 'APPROVED'::character varying, 'DISPUTED'::character varying, 'PAID'::character varying])::text[])))
);


ALTER TABLE public.milestones OWNER TO neondb_owner;

--
-- Name: offer_milestones; Type: TABLE; Schema: public; Owner: neondb_owner
--

CREATE TABLE public.offer_milestones (
    id uuid NOT NULL,
    amount numeric(10,2) NOT NULL,
    description text,
    due_date date NOT NULL,
    status public.offer_milestone_status NOT NULL,
    title character varying(255) NOT NULL,
    offer_id uuid NOT NULL
);


ALTER TABLE public.offer_milestones OWNER TO neondb_owner;

--
-- Name: offer_skills; Type: TABLE; Schema: public; Owner: neondb_owner
--

CREATE TABLE public.offer_skills (
    offer_id uuid NOT NULL,
    skill_id integer NOT NULL
);


ALTER TABLE public.offer_skills OWNER TO neondb_owner;

--
-- Name: payments; Type: TABLE; Schema: public; Owner: neondb_owner
--

CREATE TABLE public.payments (
    id uuid NOT NULL,
    amount double precision NOT NULL,
    created_at timestamp(6) without time zone,
    payment_method character varying(255),
    payment_type character varying(255) NOT NULL,
    status character varying(255) NOT NULL,
    transaction_reference character varying(255),
    updated_at timestamp(6) without time zone,
    milestone_id uuid,
    CONSTRAINT payments_payment_method_check CHECK (((payment_method)::text = ANY ((ARRAY['CREDIT_CARD'::character varying, 'PAYPAL'::character varying, 'BANK_TRANSFER'::character varying, 'PLATFORM_BALANCE'::character varying])::text[]))),
    CONSTRAINT payments_payment_type_check CHECK (((payment_type)::text = ANY ((ARRAY['ESCROW_FUNDING'::character varying, 'FREELANCER_PAYOUT'::character varying, 'REFUND'::character varying])::text[]))),
    CONSTRAINT payments_status_check CHECK (((status)::text = ANY ((ARRAY['PENDING'::character varying, 'COMPLETED'::character varying, 'FAILED'::character varying, 'REFUNDED'::character varying])::text[])))
);


ALTER TABLE public.payments OWNER TO neondb_owner;

--
-- Name: permisos; Type: TABLE; Schema: public; Owner: neondb_owner
--

CREATE TABLE public.permisos (
    id integer NOT NULL,
    name character varying(50) NOT NULL,
    CONSTRAINT permisos_name_check CHECK (((name)::text = 'EDITAR_PERFIL'::text))
);


ALTER TABLE public.permisos OWNER TO neondb_owner;

--
-- Name: permisos_id_seq; Type: SEQUENCE; Schema: public; Owner: neondb_owner
--

ALTER TABLE public.permisos ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.permisos_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: permisos_role; Type: TABLE; Schema: public; Owner: neondb_owner
--

CREATE TABLE public.permisos_role (
    id_role integer NOT NULL,
    id_permiso integer NOT NULL
);


ALTER TABLE public.permisos_role OWNER TO neondb_owner;

--
-- Name: portfolios; Type: TABLE; Schema: public; Owner: neondb_owner
--

CREATE TABLE public.portfolios (
    id uuid NOT NULL,
    description text,
    image_url character varying(255)[],
    project_url character varying(255),
    tecnologias_usadas character varying(255)[],
    title character varying(255),
    freelancer_id uuid
);


ALTER TABLE public.portfolios OWNER TO neondb_owner;

--
-- Name: pyme_profiles; Type: TABLE; Schema: public; Owner: neondb_owner
--

CREATE TABLE public.pyme_profiles (
    user_id uuid NOT NULL,
    company_name character varying(255),
    industry character varying(255),
    reputation_score double precision,
    verification_badge boolean
);


ALTER TABLE public.pyme_profiles OWNER TO neondb_owner;

--
-- Name: roles; Type: TABLE; Schema: public; Owner: neondb_owner
--

CREATE TABLE public.roles (
    id integer NOT NULL,
    name character varying(20) NOT NULL,
    CONSTRAINT roles_name_check CHECK (((name)::text = ANY ((ARRAY['FREELANCER'::character varying, 'PYME'::character varying, 'ADMIN'::character varying])::text[])))
);


ALTER TABLE public.roles OWNER TO neondb_owner;

--
-- Name: roles_id_seq; Type: SEQUENCE; Schema: public; Owner: neondb_owner
--

ALTER TABLE public.roles ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.roles_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: silabo; Type: TABLE; Schema: public; Owner: neondb_owner
--

CREATE TABLE public.silabo (
    id integer NOT NULL,
    description text,
    punto character varying(60) NOT NULL,
    skill_id integer NOT NULL
);


ALTER TABLE public.silabo OWNER TO neondb_owner;

--
-- Name: silabo_id_seq; Type: SEQUENCE; Schema: public; Owner: neondb_owner
--

ALTER TABLE public.silabo ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.silabo_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: skills; Type: TABLE; Schema: public; Owner: neondb_owner
--

CREATE TABLE public.skills (
    id integer NOT NULL,
    category character varying(255),
    name character varying(255) NOT NULL
);


ALTER TABLE public.skills OWNER TO neondb_owner;

--
-- Name: skills_id_seq; Type: SEQUENCE; Schema: public; Owner: neondb_owner
--

ALTER TABLE public.skills ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.skills_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: specialties; Type: TABLE; Schema: public; Owner: neondb_owner
--

CREATE TABLE public.specialties (
    id integer NOT NULL,
    description text,
    name character varying(255) NOT NULL
);


ALTER TABLE public.specialties OWNER TO neondb_owner;

--
-- Name: specialties_id_seq; Type: SEQUENCE; Schema: public; Owner: neondb_owner
--

ALTER TABLE public.specialties ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.specialties_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: specialty_skills; Type: TABLE; Schema: public; Owner: neondb_owner
--

CREATE TABLE public.specialty_skills (
    specialty_id integer NOT NULL,
    skill_id integer NOT NULL
);


ALTER TABLE public.specialty_skills OWNER TO neondb_owner;

--
-- Name: submission_attachments; Type: TABLE; Schema: public; Owner: neondb_owner
--

CREATE TABLE public.submission_attachments (
    id uuid NOT NULL,
    file_name character varying(255) NOT NULL,
    file_type character varying(50),
    file_url character varying(512) NOT NULL,
    uploaded_at timestamp(6) without time zone,
    phase_id uuid NOT NULL
);


ALTER TABLE public.submission_attachments OWNER TO neondb_owner;

--
-- Name: users; Type: TABLE; Schema: public; Owner: neondb_owner
--

CREATE TABLE public.users (
    id uuid NOT NULL,
    created_at timestamp(6) without time zone,
    email character varying(255) NOT NULL,
    is_active boolean NOT NULL,
    password character varying(255),
    updated_at timestamp(6) without time zone,
    role_id integer
);


ALTER TABLE public.users OWNER TO neondb_owner;

--
-- Data for Name: applications; Type: TABLE DATA; Schema: public; Owner: neondb_owner
--

COPY public.applications (id, estimated_days, proposed_amount, status, freelancer_id, offer_id) FROM stdin;
63d06863-ef0f-4065-b616-ff56e56c778d	15	1500	ACCEPTED	1dfe771f-dedb-4ea0-bbdf-ae91dd521961	e89a5a5c-f411-41f6-8e1b-da274e94911d
\.


--
-- Data for Name: assessments; Type: TABLE DATA; Schema: public; Owner: neondb_owner
--

COPY public.assessments (id, passed, quiz_data, score, freelancer_id, skill_id) FROM stdin;
4d34d5a6-d890-4c0a-8147-fba6ba8a15d6	t	{}	90	1dfe771f-dedb-4ea0-bbdf-ae91dd521961	1
4f2b1ee8-d304-4738-8879-8f51c6743569	t	{}	85	1dfe771f-dedb-4ea0-bbdf-ae91dd521961	2
abff09de-080f-4bea-9f12-90214f13f03f	\N	{"skill": "Java", "questions": [{"options": ["Manejo robusto de lógica de negocio y persistencia", "Renderizado de componentes visuales", "Diseño de paletas de colores", "Compresión de imágenes"], "question": "¿Cuál es un beneficio clave de usar Java en el backend?", "correctIndex": 0}, {"options": ["Arquitectura en capas (controller/service/repository)", "Todo el código en un único archivo", "Mezclar SQL directamente en la interfaz gráfica", "Ignorar la inyección de dependencias"], "question": "¿Qué patrón se usa comúnmente para separar responsabilidades en aplicaciones con Java?", "correctIndex": 0}, {"options": ["Autenticación y autorización basada en tokens (JWT)", "Dejar todos los endpoints públicos", "Usar solo variables globales", "Deshabilitar HTTPS"], "question": "¿Qué mecanismo se usa típicamente para proteger endpoints en Java?", "correctIndex": 0}, {"options": ["Un conjunto de operaciones que se aplican de forma atómica", "Un archivo de configuración", "Un componente visual", "Una hoja de estilos"], "question": "¿Qué es una transacción en el contexto de Java y bases de datos?", "correctIndex": 0}, {"options": ["Pruebas unitarias con mocks de las dependencias", "Pruebas manuales exclusivamente", "Ninguna prueba es necesaria", "Solo pruebas de estilos CSS"], "question": "¿Qué tipo de pruebas son más adecuadas para validar la lógica de un servicio en Java?", "correctIndex": 0}]}	\N	1dfe771f-dedb-4ea0-bbdf-ae91dd521961	1
165ade93-9d15-427a-8c13-e7c771c75e38	t	{"skill": "Java", "questions": [{"options": ["Manejo robusto de lógica de negocio y persistencia", "Renderizado de componentes visuales", "Diseño de paletas de colores", "Compresión de imágenes"], "question": "¿Cuál es un beneficio clave de usar Java en el backend?", "correctIndex": 0}, {"options": ["Arquitectura en capas (controller/service/repository)", "Todo el código en un único archivo", "Mezclar SQL directamente en la interfaz gráfica", "Ignorar la inyección de dependencias"], "question": "¿Qué patrón se usa comúnmente para separar responsabilidades en aplicaciones con Java?", "correctIndex": 0}, {"options": ["Autenticación y autorización basada en tokens (JWT)", "Dejar todos los endpoints públicos", "Usar solo variables globales", "Deshabilitar HTTPS"], "question": "¿Qué mecanismo se usa típicamente para proteger endpoints en Java?", "correctIndex": 0}, {"options": ["Un conjunto de operaciones que se aplican de forma atómica", "Un archivo de configuración", "Un componente visual", "Una hoja de estilos"], "question": "¿Qué es una transacción en el contexto de Java y bases de datos?", "correctIndex": 0}, {"options": ["Pruebas unitarias con mocks de las dependencias", "Pruebas manuales exclusivamente", "Ninguna prueba es necesaria", "Solo pruebas de estilos CSS"], "question": "¿Qué tipo de pruebas son más adecuadas para validar la lógica de un servicio en Java?", "correctIndex": 0}]}	100	1dfe771f-dedb-4ea0-bbdf-ae91dd521961	1
69bc978d-d006-4854-86e4-f88e0b02a3e4	\N	{"skill": "Docker", "questions": [{"options": ["Empaquetar y aislar aplicaciones en contenedores", "Diseñar interfaces gráficas", "Ejecutar consultas SQL", "Renderizar componentes en el navegador"], "question": "¿Cuál es el objetivo principal de Docker?", "correctIndex": 0}, {"options": ["Un Dockerfile", "Un archivo .css", "Un archivo .jsx", "Un archivo .sql"], "question": "¿Qué archivo define cómo se construye una imagen en Docker?", "correctIndex": 0}, {"options": ["docker ps", "docker delete", "docker paint", "docker fetch"], "question": "¿Qué comando permite ver los contenedores en ejecución?", "correctIndex": 0}, {"options": ["Escalado y recuperación automática de servicios", "Mayor tamaño de las imágenes", "Menor portabilidad del código", "Acoplamiento fuerte entre servicios"], "question": "¿Qué ventaja ofrece la orquestación de contenedores en producción?", "correctIndex": 0}, {"options": ["Usar imágenes base ligeras (multi-stage build)", "Incluir todas las dependencias de desarrollo", "Evitar el uso de .dockerignore", "Copiar el repositorio completo sin filtrar"], "question": "¿Qué práctica reduce el tamaño de una imagen de contenedor?", "correctIndex": 0}]}	\N	1dfe771f-dedb-4ea0-bbdf-ae91dd521961	4
fb0f9f27-5bdd-48aa-97e6-c4f8dec311d9	\N	{"skill": "Docker", "questions": [{"options": ["Empaquetar y aislar aplicaciones en contenedores", "Diseñar interfaces gráficas", "Ejecutar consultas SQL", "Renderizar componentes en el navegador"], "question": "¿Cuál es el objetivo principal de Docker?", "correctIndex": 0}, {"options": ["Un Dockerfile", "Un archivo .css", "Un archivo .jsx", "Un archivo .sql"], "question": "¿Qué archivo define cómo se construye una imagen en Docker?", "correctIndex": 0}, {"options": ["docker ps", "docker delete", "docker paint", "docker fetch"], "question": "¿Qué comando permite ver los contenedores en ejecución?", "correctIndex": 0}, {"options": ["Escalado y recuperación automática de servicios", "Mayor tamaño de las imágenes", "Menor portabilidad del código", "Acoplamiento fuerte entre servicios"], "question": "¿Qué ventaja ofrece la orquestación de contenedores en producción?", "correctIndex": 0}, {"options": ["Usar imágenes base ligeras (multi-stage build)", "Incluir todas las dependencias de desarrollo", "Evitar el uso de .dockerignore", "Copiar el repositorio completo sin filtrar"], "question": "¿Qué práctica reduce el tamaño de una imagen de contenedor?", "correctIndex": 0}]}	\N	1dfe771f-dedb-4ea0-bbdf-ae91dd521961	4
\.


--
-- Data for Name: audit_logs; Type: TABLE DATA; Schema: public; Owner: neondb_owner
--

COPY public.audit_logs (id, action, changed_by_user_id, ip_address, new_data, old_data, record_id, table_name, "timestamp") FROM stdin;
\.


--
-- Data for Name: contracts; Type: TABLE DATA; Schema: public; Owner: neondb_owner
--

COPY public.contracts (id, digital_signature_freelancer, digital_signature_pyme, status, application_id) FROM stdin;
81726690-6434-4038-bc6d-c67416471af3	Signature_Freelancer_Doe	Signature_Pyme_Acme	SIGNED	63d06863-ef0f-4065-b616-ff56e56c778d
\.


--
-- Data for Name: deliverables; Type: TABLE DATA; Schema: public; Owner: neondb_owner
--

COPY public.deliverables (id, evidence_url, notes, submitted_at, milestone_id) FROM stdin;
\.


--
-- Data for Name: disputes; Type: TABLE DATA; Schema: public; Owner: neondb_owner
--

COPY public.disputes (id, ai_resolution_report, status, milestone_id) FROM stdin;
\.


--
-- Data for Name: evaluation_phase_templates; Type: TABLE DATA; Schema: public; Owner: neondb_owner
--

COPY public.evaluation_phase_templates (id, instructions_prompt, linked_silabo_ids, order_index, passing_score, phase_type, time_limit_minutes, title, specialty_id) FROM stdin;
\.


--
-- Data for Name: evaluation_phases; Type: TABLE DATA; Schema: public; Owner: neondb_owner
--

COPY public.evaluation_phases (id, ai_feedback, ended_at, phase_score, phase_type, plagiarism_summary, started_at, status, submitted_at, time_spent_seconds, user_response_data, session_id) FROM stdin;
\.


--
-- Data for Name: evaluation_sessions; Type: TABLE DATA; Schema: public; Owner: neondb_owner
--

COPY public.evaluation_sessions (id, completed_at, final_score, is_passed, started_at, status, freelancer_id, freelancer_specialty_id) FROM stdin;
\.


--
-- Data for Name: fraud_telemetry_logs; Type: TABLE DATA; Schema: public; Owner: neondb_owner
--

COPY public.fraud_telemetry_logs (id, event_type, metadata, "timestamp", session_id) FROM stdin;
\.


--
-- Data for Name: freelancer_profiles; Type: TABLE DATA; Schema: public; Owner: neondb_owner
--

COPY public.freelancer_profiles (user_id, bio, first_name, last_name) FROM stdin;
58db7173-f355-4440-b69b-b493467a379a	Programador apasionado con 2 años de experiencia	Israel Joel	Muñoz Rodriguez
1dfe771f-dedb-4ea0-bbdf-ae91dd521961	Experienced Java developer	John	Doe
4899e61e-deb9-413f-9d84-11869876db6e			
0107dbdb-63af-43f6-95f2-8a6400a04543			
90ca2dae-b830-4254-9268-31feefb602b7			
\.


--
-- Data for Name: freelancer_skills; Type: TABLE DATA; Schema: public; Owner: neondb_owner
--

COPY public.freelancer_skills (freelancer_id, skill_id) FROM stdin;
1dfe771f-dedb-4ea0-bbdf-ae91dd521961	4
1dfe771f-dedb-4ea0-bbdf-ae91dd521961	1
1dfe771f-dedb-4ea0-bbdf-ae91dd521961	2
\.


--
-- Data for Name: freelancer_specialties; Type: TABLE DATA; Schema: public; Owner: neondb_owner
--

COPY public.freelancer_specialties (id, achieved_at, enrolled_at, freelancer_id, specialty_id) FROM stdin;
\.


--
-- Data for Name: job_offers; Type: TABLE DATA; Schema: public; Owner: neondb_owner
--

COPY public.job_offers (id, budget_type, description, status, title, total_budget, pyme_id, estimated_days, location, max_budget, min_budget, published_at, modality, project_category, specialty_id) FROM stdin;
e89a5a5c-f411-41f6-8e1b-da274e94911d	FIJO	Buscamos un desarrollador backend con experiencia en Java y Spring Boot.	ABIERTA	Desarrollador Java y Spring Boot (100% Match)	1500	1dda8b64-b432-411b-8bc8-50eae87b9b16	\N	\N	\N	\N	\N	\N	\N	\N
51d605fe-16e4-4f95-ac72-e0585a3b9c61	POR_HORA	Buscamos desarrollador para migrar portal heredado a React con backend Java.	ABIERTA	Desarrollador Fullstack Java y React (Skill Gap)	2500	1dda8b64-b432-411b-8bc8-50eae87b9b16	\N	\N	\N	\N	\N	\N	\N	\N
\.


--
-- Data for Name: milestones; Type: TABLE DATA; Schema: public; Owner: neondb_owner
--

COPY public.milestones (id, amount, deadline, status, title, contract_id) FROM stdin;
3cc35c8d-fc83-4f63-8794-7ffa110abc65	750	2026-07-14	PENDING_FUNDING	Desarrollo de APIs e Integración de Seguridad	81726690-6434-4038-bc6d-c67416471af3
\.


--
-- Data for Name: offer_milestones; Type: TABLE DATA; Schema: public; Owner: neondb_owner
--

COPY public.offer_milestones (id, amount, description, due_date, status, title, offer_id) FROM stdin;
\.


--
-- Data for Name: offer_skills; Type: TABLE DATA; Schema: public; Owner: neondb_owner
--

COPY public.offer_skills (offer_id, skill_id) FROM stdin;
e89a5a5c-f411-41f6-8e1b-da274e94911d	1
e89a5a5c-f411-41f6-8e1b-da274e94911d	2
51d605fe-16e4-4f95-ac72-e0585a3b9c61	1
51d605fe-16e4-4f95-ac72-e0585a3b9c61	3
\.


--
-- Data for Name: payments; Type: TABLE DATA; Schema: public; Owner: neondb_owner
--

COPY public.payments (id, amount, created_at, payment_method, payment_type, status, transaction_reference, updated_at, milestone_id) FROM stdin;
\.


--
-- Data for Name: permisos; Type: TABLE DATA; Schema: public; Owner: neondb_owner
--

COPY public.permisos (id, name) FROM stdin;
\.


--
-- Data for Name: permisos_role; Type: TABLE DATA; Schema: public; Owner: neondb_owner
--

COPY public.permisos_role (id_role, id_permiso) FROM stdin;
\.


--
-- Data for Name: portfolios; Type: TABLE DATA; Schema: public; Owner: neondb_owner
--

COPY public.portfolios (id, description, image_url, project_url, tecnologias_usadas, title, freelancer_id) FROM stdin;
20d59ce4-205e-47a0-96a8-691f65bf919e	Permite a freelancers conectarse con pymes para trabajar	{"https://sfo3.digitaloceanspaces.com/freelancerpymes/a9b95e74-4fdc-426d-84fb-98824b66a3cd-LOGO CEIS.jpg",https://sfo3.digitaloceanspaces.com/freelancerpymes/31f7e267-834b-40c8-92d5-039246579255-kintsugi.jpg}	http://localhost:3000/freelancer/portafolio	{nextjs,"spring boot",neon,"DigitalOcean Space"}	Pymes y freelancers	58db7173-f355-4440-b69b-b493467a379a
127db981-b8d2-413e-8818-e4e66a79997f	Gestion de material estudiantil y trabajo academicos para profesores y alumnado	{https://7649d11884d4e4cdbd2ac86deb11486e.r2.cloudflarestorage.com/pymes/13dab031-568b-4ca0-b0c5-0ba8badd458f-Curso_online.jpg,https://7649d11884d4e4cdbd2ac86deb11486e.r2.cloudflarestorage.com/pymes/5ea9b8ab-d0f7-4205-ab72-0f90b12ec4b3-aula_virtual.jpg}	https://aula-virtual-seminario-mpwz.vercel.app/	{"Spring boot",Go,Postgresql}	Aula virtual seminario	1dfe771f-dedb-4ea0-bbdf-ae91dd521961
\.


--
-- Data for Name: pyme_profiles; Type: TABLE DATA; Schema: public; Owner: neondb_owner
--

COPY public.pyme_profiles (user_id, company_name, industry, reputation_score, verification_badge) FROM stdin;
1dda8b64-b432-411b-8bc8-50eae87b9b16	Acme Software	Information Technology	4.8	t
\.


--
-- Data for Name: roles; Type: TABLE DATA; Schema: public; Owner: neondb_owner
--

COPY public.roles (id, name) FROM stdin;
1	FREELANCER
2	PYME
3	ADMIN
\.


--
-- Data for Name: silabo; Type: TABLE DATA; Schema: public; Owner: neondb_owner
--

COPY public.silabo (id, description, punto, skill_id) FROM stdin;
\.


--
-- Data for Name: skills; Type: TABLE DATA; Schema: public; Owner: neondb_owner
--

COPY public.skills (id, category, name) FROM stdin;
1	Backend	Java
2	Backend	Spring Boot
3	Frontend	React
4	DevOps	Docker
\.


--
-- Data for Name: specialties; Type: TABLE DATA; Schema: public; Owner: neondb_owner
--

COPY public.specialties (id, description, name) FROM stdin;
\.


--
-- Data for Name: specialty_skills; Type: TABLE DATA; Schema: public; Owner: neondb_owner
--

COPY public.specialty_skills (specialty_id, skill_id) FROM stdin;
\.


--
-- Data for Name: submission_attachments; Type: TABLE DATA; Schema: public; Owner: neondb_owner
--

COPY public.submission_attachments (id, file_name, file_type, file_url, uploaded_at, phase_id) FROM stdin;
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: neondb_owner
--

COPY public.users (id, created_at, email, is_active, password, updated_at, role_id) FROM stdin;
58db7173-f355-4440-b69b-b493467a379a	\N	israjmr19@gmail.com	t	123	\N	1
1dfe771f-dedb-4ea0-bbdf-ae91dd521961	2026-07-04 15:07:54.817964	freelancer@example.com	t	$2a$10$zKDku6cWS14HKHkHEVp3r.TXoyd3bM14gSBgC.BcfS0KuTLSyMMtS	2026-07-04 15:07:54.817964	1
1dda8b64-b432-411b-8bc8-50eae87b9b16	2026-07-04 15:07:56.639081	pyme@example.com	t	$2a$10$J5CuAi7dQg2BaKSrISDVIuEyv7xgTe/dbqt7QMbzE6f/uiYr1D2bG	2026-07-04 15:07:56.639081	2
4899e61e-deb9-413f-9d84-11869876db6e	2026-07-06 14:22:06.267233	test@domain.com	t	$2a$10$toWJVJSHsTOgeyRovymJ9.Pg0RTG5y3GtFNbK6EGB9FKAOVmX3syG	2026-07-06 14:22:06.267233	1
0107dbdb-63af-43f6-95f2-8a6400a04543	2026-07-06 14:32:06.514432	test2@domain.com	t	$2a$10$ABWy8IkNl8k8eRun45Q4NO/HqQKl/.CkbCgJz.10xDS/FGijHDvwi	2026-07-06 14:32:06.514432	1
90ca2dae-b830-4254-9268-31feefb602b7	2026-07-06 14:33:19.847833	test4@domain.com	t	$2a$10$VTvBpfWAq8LP1Rx7tumsU.btaOrCCgeFuHO5P3pRB3hhF.gHvXrRa	2026-07-06 14:33:19.847833	1
\.


--
-- Name: permisos_id_seq; Type: SEQUENCE SET; Schema: public; Owner: neondb_owner
--

SELECT pg_catalog.setval('public.permisos_id_seq', 1, false);


--
-- Name: roles_id_seq; Type: SEQUENCE SET; Schema: public; Owner: neondb_owner
--

SELECT pg_catalog.setval('public.roles_id_seq', 3, true);


--
-- Name: silabo_id_seq; Type: SEQUENCE SET; Schema: public; Owner: neondb_owner
--

SELECT pg_catalog.setval('public.silabo_id_seq', 1, false);


--
-- Name: skills_id_seq; Type: SEQUENCE SET; Schema: public; Owner: neondb_owner
--

SELECT pg_catalog.setval('public.skills_id_seq', 4, true);


--
-- Name: specialties_id_seq; Type: SEQUENCE SET; Schema: public; Owner: neondb_owner
--

SELECT pg_catalog.setval('public.specialties_id_seq', 1, false);


--
-- Name: applications applications_pkey; Type: CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.applications
    ADD CONSTRAINT applications_pkey PRIMARY KEY (id);


--
-- Name: assessments assessments_pkey; Type: CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.assessments
    ADD CONSTRAINT assessments_pkey PRIMARY KEY (id);


--
-- Name: audit_logs audit_logs_pkey; Type: CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.audit_logs
    ADD CONSTRAINT audit_logs_pkey PRIMARY KEY (id);


--
-- Name: contracts contracts_pkey; Type: CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.contracts
    ADD CONSTRAINT contracts_pkey PRIMARY KEY (id);


--
-- Name: deliverables deliverables_pkey; Type: CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.deliverables
    ADD CONSTRAINT deliverables_pkey PRIMARY KEY (id);


--
-- Name: disputes disputes_pkey; Type: CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.disputes
    ADD CONSTRAINT disputes_pkey PRIMARY KEY (id);


--
-- Name: evaluation_phase_templates evaluation_phase_templates_pkey; Type: CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.evaluation_phase_templates
    ADD CONSTRAINT evaluation_phase_templates_pkey PRIMARY KEY (id);


--
-- Name: evaluation_phases evaluation_phases_pkey; Type: CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.evaluation_phases
    ADD CONSTRAINT evaluation_phases_pkey PRIMARY KEY (id);


--
-- Name: evaluation_sessions evaluation_sessions_pkey; Type: CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.evaluation_sessions
    ADD CONSTRAINT evaluation_sessions_pkey PRIMARY KEY (id);


--
-- Name: fraud_telemetry_logs fraud_telemetry_logs_pkey; Type: CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.fraud_telemetry_logs
    ADD CONSTRAINT fraud_telemetry_logs_pkey PRIMARY KEY (id);


--
-- Name: freelancer_profiles freelancer_profiles_pkey; Type: CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.freelancer_profiles
    ADD CONSTRAINT freelancer_profiles_pkey PRIMARY KEY (user_id);


--
-- Name: freelancer_skills freelancer_skills_pkey; Type: CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.freelancer_skills
    ADD CONSTRAINT freelancer_skills_pkey PRIMARY KEY (freelancer_id, skill_id);


--
-- Name: freelancer_specialties freelancer_specialties_pkey; Type: CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.freelancer_specialties
    ADD CONSTRAINT freelancer_specialties_pkey PRIMARY KEY (id);


--
-- Name: job_offers job_offers_pkey; Type: CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.job_offers
    ADD CONSTRAINT job_offers_pkey PRIMARY KEY (id);


--
-- Name: milestones milestones_pkey; Type: CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.milestones
    ADD CONSTRAINT milestones_pkey PRIMARY KEY (id);


--
-- Name: offer_milestones offer_milestones_pkey; Type: CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.offer_milestones
    ADD CONSTRAINT offer_milestones_pkey PRIMARY KEY (id);


--
-- Name: offer_skills offer_skills_pkey; Type: CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.offer_skills
    ADD CONSTRAINT offer_skills_pkey PRIMARY KEY (offer_id, skill_id);


--
-- Name: payments payments_pkey; Type: CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.payments
    ADD CONSTRAINT payments_pkey PRIMARY KEY (id);


--
-- Name: permisos permisos_pkey; Type: CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.permisos
    ADD CONSTRAINT permisos_pkey PRIMARY KEY (id);


--
-- Name: permisos_role permisos_role_pkey; Type: CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.permisos_role
    ADD CONSTRAINT permisos_role_pkey PRIMARY KEY (id_role, id_permiso);


--
-- Name: portfolios portfolios_pkey; Type: CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.portfolios
    ADD CONSTRAINT portfolios_pkey PRIMARY KEY (id);


--
-- Name: pyme_profiles pyme_profiles_pkey; Type: CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.pyme_profiles
    ADD CONSTRAINT pyme_profiles_pkey PRIMARY KEY (user_id);


--
-- Name: roles roles_pkey; Type: CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (id);


--
-- Name: silabo silabo_pkey; Type: CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.silabo
    ADD CONSTRAINT silabo_pkey PRIMARY KEY (id);


--
-- Name: skills skills_pkey; Type: CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.skills
    ADD CONSTRAINT skills_pkey PRIMARY KEY (id);


--
-- Name: specialties specialties_pkey; Type: CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.specialties
    ADD CONSTRAINT specialties_pkey PRIMARY KEY (id);


--
-- Name: specialty_skills specialty_skills_pkey; Type: CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.specialty_skills
    ADD CONSTRAINT specialty_skills_pkey PRIMARY KEY (specialty_id, skill_id);


--
-- Name: submission_attachments submission_attachments_pkey; Type: CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.submission_attachments
    ADD CONSTRAINT submission_attachments_pkey PRIMARY KEY (id);


--
-- Name: disputes uk37om9o896cod19fbnaxn9d3fw; Type: CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.disputes
    ADD CONSTRAINT uk37om9o896cod19fbnaxn9d3fw UNIQUE (milestone_id);


--
-- Name: users uk6dotkott2kjsp8vw4d0m25fb7; Type: CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT uk6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email);


--
-- Name: contracts ukbebeel5e0t9wrfenp2wxqdq7l; Type: CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.contracts
    ADD CONSTRAINT ukbebeel5e0t9wrfenp2wxqdq7l UNIQUE (application_id);


--
-- Name: evaluation_phase_templates ukjrw9984lhgolea35fucad3eb7; Type: CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.evaluation_phase_templates
    ADD CONSTRAINT ukjrw9984lhgolea35fucad3eb7 UNIQUE (specialty_id, order_index);


--
-- Name: payments ukrwn36natqiwaseu5c3jvaun3; Type: CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.payments
    ADD CONSTRAINT ukrwn36natqiwaseu5c3jvaun3 UNIQUE (transaction_reference);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: applications fk19d05w7gonhxnajgdiedu7of; Type: FK CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.applications
    ADD CONSTRAINT fk19d05w7gonhxnajgdiedu7of FOREIGN KEY (freelancer_id) REFERENCES public.freelancer_profiles(user_id);


--
-- Name: milestones fk1d8tkd27ry53x8jmyf6v4dc3; Type: FK CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.milestones
    ADD CONSTRAINT fk1d8tkd27ry53x8jmyf6v4dc3 FOREIGN KEY (contract_id) REFERENCES public.contracts(id);


--
-- Name: pyme_profiles fk23tbvuvgfoeds33m8it9govp6; Type: FK CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.pyme_profiles
    ADD CONSTRAINT fk23tbvuvgfoeds33m8it9govp6 FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: assessments fk2eb58l2mtpavfd2svtrhk3so5; Type: FK CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.assessments
    ADD CONSTRAINT fk2eb58l2mtpavfd2svtrhk3so5 FOREIGN KEY (skill_id) REFERENCES public.skills(id);


--
-- Name: job_offers fk4gok69lsmpwiefi2kf6vih8av; Type: FK CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.job_offers
    ADD CONSTRAINT fk4gok69lsmpwiefi2kf6vih8av FOREIGN KEY (specialty_id) REFERENCES public.specialties(id);


--
-- Name: offer_skills fk8jirbhgjlfput8wi2jsp702w; Type: FK CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.offer_skills
    ADD CONSTRAINT fk8jirbhgjlfput8wi2jsp702w FOREIGN KEY (offer_id) REFERENCES public.job_offers(id);


--
-- Name: fraud_telemetry_logs fk9346gms2nrq6iho13baqrvy7h; Type: FK CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.fraud_telemetry_logs
    ADD CONSTRAINT fk9346gms2nrq6iho13baqrvy7h FOREIGN KEY (session_id) REFERENCES public.evaluation_sessions(id);


--
-- Name: permisos_role fk9as5i546chn6hbrbt4a1mseov; Type: FK CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.permisos_role
    ADD CONSTRAINT fk9as5i546chn6hbrbt4a1mseov FOREIGN KEY (id_role) REFERENCES public.roles(id);


--
-- Name: applications fk9cn0pf50n7784s4m9ibr2e3is; Type: FK CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.applications
    ADD CONSTRAINT fk9cn0pf50n7784s4m9ibr2e3is FOREIGN KEY (offer_id) REFERENCES public.job_offers(id);


--
-- Name: job_offers fk9urcpx0u6bpn28cv5no0n8ick; Type: FK CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.job_offers
    ADD CONSTRAINT fk9urcpx0u6bpn28cv5no0n8ick FOREIGN KEY (pyme_id) REFERENCES public.pyme_profiles(user_id);


--
-- Name: freelancer_skills fka8qexj5k6v7fiw1wo0re4thec; Type: FK CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.freelancer_skills
    ADD CONSTRAINT fka8qexj5k6v7fiw1wo0re4thec FOREIGN KEY (freelancer_id) REFERENCES public.freelancer_profiles(user_id);


--
-- Name: submission_attachments fkcddstc96v0qlywj1877ehhwvg; Type: FK CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.submission_attachments
    ADD CONSTRAINT fkcddstc96v0qlywj1877ehhwvg FOREIGN KEY (phase_id) REFERENCES public.evaluation_phases(id);


--
-- Name: evaluation_sessions fkco25a9lauap5gdac1w5w37fqc; Type: FK CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.evaluation_sessions
    ADD CONSTRAINT fkco25a9lauap5gdac1w5w37fqc FOREIGN KEY (freelancer_id) REFERENCES public.freelancer_profiles(user_id);


--
-- Name: freelancer_profiles fkd173yjyeljsmurg3rtgx3yar3; Type: FK CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.freelancer_profiles
    ADD CONSTRAINT fkd173yjyeljsmurg3rtgx3yar3 FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: freelancer_specialties fkf7m84avppxhh5trny60lu9nk1; Type: FK CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.freelancer_specialties
    ADD CONSTRAINT fkf7m84avppxhh5trny60lu9nk1 FOREIGN KEY (specialty_id) REFERENCES public.specialties(id);


--
-- Name: specialty_skills fkffdv2oeym4u3b01fsq40bbyhv; Type: FK CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.specialty_skills
    ADD CONSTRAINT fkffdv2oeym4u3b01fsq40bbyhv FOREIGN KEY (skill_id) REFERENCES public.skills(id);


--
-- Name: disputes fkg8xb4x1rr1putpeocrdb70uuw; Type: FK CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.disputes
    ADD CONSTRAINT fkg8xb4x1rr1putpeocrdb70uuw FOREIGN KEY (milestone_id) REFERENCES public.milestones(id);


--
-- Name: evaluation_phase_templates fkgeardhyiyg7vmclecpe4bcm2e; Type: FK CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.evaluation_phase_templates
    ADD CONSTRAINT fkgeardhyiyg7vmclecpe4bcm2e FOREIGN KEY (specialty_id) REFERENCES public.specialties(id);


--
-- Name: permisos_role fkgqytjmrcms8lr6yrf7xke7w0r; Type: FK CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.permisos_role
    ADD CONSTRAINT fkgqytjmrcms8lr6yrf7xke7w0r FOREIGN KEY (id_permiso) REFERENCES public.permisos(id);


--
-- Name: silabo fkihpxyt91kbxgxcv3lmbfcnr2x; Type: FK CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.silabo
    ADD CONSTRAINT fkihpxyt91kbxgxcv3lmbfcnr2x FOREIGN KEY (skill_id) REFERENCES public.skills(id);


--
-- Name: contracts fkiv6pv5b1owqgdla333vo3onh6; Type: FK CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.contracts
    ADD CONSTRAINT fkiv6pv5b1owqgdla333vo3onh6 FOREIGN KEY (application_id) REFERENCES public.applications(id);


--
-- Name: evaluation_phases fkj97r5a4cmtjt68ldqmoeffhey; Type: FK CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.evaluation_phases
    ADD CONSTRAINT fkj97r5a4cmtjt68ldqmoeffhey FOREIGN KEY (session_id) REFERENCES public.evaluation_sessions(id);


--
-- Name: evaluation_sessions fkl5r42qsdog4uc6cumg1b7i967; Type: FK CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.evaluation_sessions
    ADD CONSTRAINT fkl5r42qsdog4uc6cumg1b7i967 FOREIGN KEY (freelancer_specialty_id) REFERENCES public.freelancer_specialties(id);


--
-- Name: assessments fkm8q9vve2oaoaa58ku5vhop21f; Type: FK CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.assessments
    ADD CONSTRAINT fkm8q9vve2oaoaa58ku5vhop21f FOREIGN KEY (freelancer_id) REFERENCES public.freelancer_profiles(user_id);


--
-- Name: deliverables fkmnr1vn921ilde71cu7omy4jpw; Type: FK CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.deliverables
    ADD CONSTRAINT fkmnr1vn921ilde71cu7omy4jpw FOREIGN KEY (milestone_id) REFERENCES public.milestones(id);


--
-- Name: payments fkn3n250fxoxd7tu0wa68l1tn78; Type: FK CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.payments
    ADD CONSTRAINT fkn3n250fxoxd7tu0wa68l1tn78 FOREIGN KEY (milestone_id) REFERENCES public.milestones(id);


--
-- Name: freelancer_skills fkoucm0h4n6wrf7j0rul9vi2obk; Type: FK CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.freelancer_skills
    ADD CONSTRAINT fkoucm0h4n6wrf7j0rul9vi2obk FOREIGN KEY (skill_id) REFERENCES public.skills(id);


--
-- Name: offer_skills fkp41ls9apjwc2sa0urxr3r2uep; Type: FK CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.offer_skills
    ADD CONSTRAINT fkp41ls9apjwc2sa0urxr3r2uep FOREIGN KEY (skill_id) REFERENCES public.skills(id);


--
-- Name: users fkp56c1712k691lhsyewcssf40f; Type: FK CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT fkp56c1712k691lhsyewcssf40f FOREIGN KEY (role_id) REFERENCES public.roles(id);


--
-- Name: offer_milestones fkpq9yterg37dmtnplwvej4twqm; Type: FK CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.offer_milestones
    ADD CONSTRAINT fkpq9yterg37dmtnplwvej4twqm FOREIGN KEY (offer_id) REFERENCES public.job_offers(id);


--
-- Name: freelancer_specialties fkq0k797xt0j75dxolcyquu9cnw; Type: FK CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.freelancer_specialties
    ADD CONSTRAINT fkq0k797xt0j75dxolcyquu9cnw FOREIGN KEY (freelancer_id) REFERENCES public.freelancer_profiles(user_id);


--
-- Name: specialty_skills fkqxrtgivrfvjq2yqe59ockun4d; Type: FK CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.specialty_skills
    ADD CONSTRAINT fkqxrtgivrfvjq2yqe59ockun4d FOREIGN KEY (specialty_id) REFERENCES public.specialties(id);


--
-- Name: portfolios fksd5apu19h3dy549xyaxpkfle7; Type: FK CONSTRAINT; Schema: public; Owner: neondb_owner
--

ALTER TABLE ONLY public.portfolios
    ADD CONSTRAINT fksd5apu19h3dy549xyaxpkfle7 FOREIGN KEY (freelancer_id) REFERENCES public.freelancer_profiles(user_id);


--
-- Name: DEFAULT PRIVILEGES FOR SEQUENCES; Type: DEFAULT ACL; Schema: public; Owner: cloud_admin
--

ALTER DEFAULT PRIVILEGES FOR ROLE cloud_admin IN SCHEMA public GRANT ALL ON SEQUENCES TO neon_superuser WITH GRANT OPTION;


--
-- Name: DEFAULT PRIVILEGES FOR TABLES; Type: DEFAULT ACL; Schema: public; Owner: cloud_admin
--

ALTER DEFAULT PRIVILEGES FOR ROLE cloud_admin IN SCHEMA public GRANT ALL ON TABLES TO neon_superuser WITH GRANT OPTION;


--
-- PostgreSQL database dump complete
--

\unrestrict X7GXhsO1w2bqonNXFZrw7RjMM8YMTIkRMontT7D1vD3YzCX4u5pjOofX8iULSxE

