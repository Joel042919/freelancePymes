package getajob.pymes.freelancepymes.payment.entity.enums;

public enum PaymentType {
    ESCROW_FUNDING, // Cuando la PYME paga para iniciar el hito (el dinero queda retenido)
    FREELANCER_PAYOUT, // Cuando se libera el dinero al freelancer
    REFUND // Si hay disputa y se le devuelve a la PYME
}
