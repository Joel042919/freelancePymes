-- ============================================================
-- 1. ENUM TYPES (equivalente a los Enums de Java)
-- ===========================================================

CREATE TYPE permisos_name AS ENUM ('EDITAR_PERFIL');
CREATE TYPE role_name AS ENUM ('FREELANCER', 'PYME', 'ADMIN');
CREATE TYPE audit_action AS ENUM ('INSERT', 'UPDATE', 'DELETE');
CREATE TYPE contract_status AS ENUM ('DRAFT', 'SIGNED', 'COMPLETED');
CREATE TYPE milestone_status AS ENUM ('PENDING_FUNDING', 'FUNDED', 'DELIVERED', 'APPROVED', 'DISPUTED', 'PAID');
CREATE TYPE offer_milestone_status AS ENUM ('PENDIENTE_FINANCIAMIENTO', 'FONDEADO', 'ENTREGADO', 'APROBADO', 'EN_DISPUTA', 'PAGADO');
CREATE TYPE dispute_status AS ENUM ('PENDING_AI_REVIEW', 'WAITING_PARTIES', 'ESCALATED_TO_ADMIN', 'RESOLVED');
CREATE TYPE application_status AS ENUM ('PENDING', 'ACCEPTED', 'REJECTED');
CREATE TYPE offer_status AS ENUM ('ABIERTA', 'EN_PROCESO', 'COMPLETADA', 'CANCELADA');
CREATE TYPE budget_type AS ENUM ('FIJO', 'POR_HORA');
CREATE TYPE modality_type AS ENUM ('REMOTO', 'PRESENCIAL', 'HIBRIDO');
CREATE TYPE project_category AS ENUM ('DESARROLLO_WEB', 'DESARROLLO_MOVIL', 'DISENO_GRAFICO', 'MARKETING_DIGITAL', 'CONTABILIDAD', 'CONSULTORIA', 'TRADUCCION', 'REDACCION', 'OTROS');
CREATE TYPE payment_method AS ENUM ('CREDIT_CARD', 'PAYPAL', 'BANK_TRANSFER', 'PLATFORM_BALANCE');
CREATE TYPE payment_status AS ENUM ('PENDING', 'COMPLETED', 'FAILED', 'REFUNDED');
CREATE TYPE payment_type AS ENUM ('ESCROW_FUNDING', 'FREELANCER_PAYOUT', 'REFUND');

-- ============================================================
-- 2. TABLAS PRINCIPALES
-- ============================================================

-- PERMISOS
CREATE TABLE permisos (
    id SERIAL PRIMARY KEY,
    name permisos_name NOT NULL
);

-- ROLES
CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    name role_name NOT NULL
);

-- PERMISOS_ROLE (tabla intermedia Many-to-Many)
CREATE TABLE permisos_role (
    id_role INTEGER NOT NULL REFERENCES roles(id),
    id_permiso INTEGER NOT NULL REFERENCES permisos(id),
    PRIMARY KEY (id_role, id_permiso)
);

-- SKILLS
CREATE TABLE skills (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(255)
);

-- SEED DE SKILLS TECNICAS COMUNES
INSERT INTO skills (name, category) VALUES
('Java', 'Backend'),
('Python', 'Backend'),
('JavaScript', 'Frontend'),
('TypeScript', 'Frontend'),
('React', 'Frontend'),
('Angular', 'Frontend'),
('Vue.js', 'Frontend'),
('Node.js', 'Backend'),
('Spring Boot', 'Backend'),
('Django', 'Backend'),
('PHP', 'Backend'),
('Laravel', 'Backend'),
('Ruby on Rails', 'Backend'),
('Go', 'Backend'),
('Rust', 'Backend'),
('C#', 'Backend'),
('.NET', 'Backend'),
('Kotlin', 'Mobile'),
('Swift', 'Mobile'),
('Flutter', 'Mobile'),
('React Native', 'Mobile'),
('SQL', 'Database'),
('PostgreSQL', 'Database'),
('MySQL', 'Database'),
('MongoDB', 'Database'),
('AWS', 'Cloud/DevOps'),
('Azure', 'Cloud/DevOps'),
('Google Cloud', 'Cloud/DevOps'),
('Docker', 'Cloud/DevOps'),
('Kubernetes', 'Cloud/DevOps'),
('Terraform', 'Cloud/DevOps'),
('CI/CD', 'Cloud/DevOps'),
('Git', 'Tools'),
('Linux', 'Tools'),
('HTML', 'Frontend'),
('CSS', 'Frontend'),
('Tailwind CSS', 'Frontend'),
('Machine Learning', 'Data/AI'),
('Data Science', 'Data/AI'),
('Figma', 'Design'),
('UI/UX', 'Design');

-- USERS
CREATE TABLE users (
    id UUID PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    role_id INTEGER REFERENCES roles(id)
);

-- FREELANCER PROFILES (hereda ID de users)
CREATE TABLE freelancer_profiles (
    user_id UUID PRIMARY KEY REFERENCES users(id),
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    bio TEXT
);

-- FREELANCER_SKILLS (Many-to-Many)
CREATE TABLE freelancer_skills (
    freelancer_id UUID NOT NULL REFERENCES freelancer_profiles(user_id),
    skill_id INTEGER NOT NULL REFERENCES skills(id),
    PRIMARY KEY (freelancer_id, skill_id)
);

-- PYME PROFILES (hereda ID de users)
CREATE TABLE pyme_profiles (
    user_id UUID PRIMARY KEY REFERENCES users(id),
    company_name VARCHAR(255),
    industry VARCHAR(255),
    verification_badge BOOLEAN DEFAULT FALSE,
    reputation_score DOUBLE PRECISION
);

-- JOB OFFERS
CREATE TABLE job_offers (
    id UUID PRIMARY KEY,
    pyme_id UUID NOT NULL REFERENCES pyme_profiles(user_id),
    title VARCHAR(255) NOT NULL,
    description TEXT,
    budget_type budget_type,
    modality modality_type,
    project_category project_category,
    estimated_days INTEGER,
    total_budget DOUBLE PRECISION,
    min_budget DOUBLE PRECISION,
    max_budget DOUBLE PRECISION,
    location VARCHAR(255),
    status offer_status NOT NULL DEFAULT 'ABIERTA',
    published_at DATE DEFAULT CURRENT_DATE,
    CONSTRAINT fk_job_offers_pyme FOREIGN KEY (pyme_id) REFERENCES pyme_profiles(user_id)
);

-- OFFER_SKILLS (Many-to-Many)
CREATE TABLE offer_skills (
    offer_id UUID NOT NULL REFERENCES job_offers(id),
    skill_id INTEGER NOT NULL REFERENCES skills(id),
    PRIMARY KEY (offer_id, skill_id)
);

-- APPLICATIONS
CREATE TABLE applications (
    id UUID PRIMARY KEY,
    offer_id UUID NOT NULL REFERENCES job_offers(id),
    freelancer_id UUID NOT NULL REFERENCES freelancer_profiles(user_id),
    proposed_amount DOUBLE PRECISION,
    estimated_days INTEGER,
    status application_status
);

-- CONTRACTS
CREATE TABLE contracts (
    id UUID PRIMARY KEY,
    application_id UUID UNIQUE NOT NULL REFERENCES applications(id),
    digital_signature_pyme VARCHAR(255),
    digital_signature_freelancer VARCHAR(255),
    status contract_status
);
-- MILESTONES
CREATE TABLE milestones (
    id UUID PRIMARY KEY,
    contract_id UUID NOT NULL REFERENCES contracts(id),
    title VARCHAR(255),
    amount DOUBLE PRECISION,
    deadline DATE,
    status milestone_status
);

-- OFFER MILESTONES (hitos definidos al publicar la oferta)
CREATE TABLE offer_milestones (
    id UUID PRIMARY KEY,
    offer_id UUID NOT NULL REFERENCES job_offers(id),
    title VARCHAR(255) NOT NULL,
    description TEXT,
    amount DOUBLE PRECISION NOT NULL,
    status offer_milestone_status NOT NULL DEFAULT 'PENDIENTE_FINANCIAMIENTO',
    due_date DATE NOT NULL
);

-- DELIVERABLES
CREATE TABLE deliverables (
    id UUID PRIMARY KEY,
    milestone_id UUID NOT NULL REFERENCES milestones(id),
    evidence_url VARCHAR(255),
    notes TEXT,
    submitted_at TIMESTAMP
);

-- DISPUTES
CREATE TABLE disputes (
    id UUID PRIMARY KEY,
    milestone_id UUID UNIQUE NOT NULL REFERENCES milestones(id),
    ai_resolution_report JSONB,
    status dispute_status
);

-- PAYMENTS
CREATE TABLE payments (
    id UUID PRIMARY KEY,
    milestone_id UUID NOT NULL REFERENCES milestones(id),
    amount DOUBLE PRECISION NOT NULL,
    payment_type payment_type NOT NULL,
    payment_method payment_method,
    transaction_reference VARCHAR(255) UNIQUE,
    status payment_status NOT NULL,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP
);

-- ASSESSMENTS (evaluaciones)
CREATE TABLE assessments (
    id UUID PRIMARY KEY,
    freelancer_id UUID NOT NULL REFERENCES freelancer_profiles(user_id),
    skill_id INTEGER REFERENCES skills(id),
    quiz_data JSONB,
    score DOUBLE PRECISION,
    passed BOOLEAN
);

-- PORTFOLIOS
CREATE TABLE portfolios (
    id UUID PRIMARY KEY,
    freelancer_id UUID NOT NULL REFERENCES freelancer_profiles(user_id),
    title VARCHAR(255),
    description TEXT,
    image_url TEXT[],          -- array de textos
    project_url VARCHAR(255),
    tecnologias_usadas TEXT[]  -- array de textos
);

-- AUDIT LOGS
CREATE TABLE audit_logs (
    id UUID PRIMARY KEY,
    table_name VARCHAR(255),
    record_id UUID,
    action audit_action,
    old_data JSONB,
    new_data JSONB,
    changed_by_user_id UUID,
    ip_address VARCHAR(45),
    timestamp TIMESTAMP
);