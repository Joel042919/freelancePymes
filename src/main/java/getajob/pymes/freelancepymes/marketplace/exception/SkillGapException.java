package getajob.pymes.freelancepymes.marketplace.exception;

import getajob.pymes.freelancepymes.profile.entity.Skill;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class SkillGapException extends RuntimeException {
    private final List<Skill> missingSkills;
    private final Map<String, Object> studyPlans;

    public SkillGapException(List<Skill> missingSkills, Map<String, Object> studyPlans) {
        super("No cumples con todas las habilidades validadas requeridas para esta oferta.");
        this.missingSkills = missingSkills;
        this.studyPlans = studyPlans;
    }
}
