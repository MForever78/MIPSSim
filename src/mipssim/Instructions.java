package mipssim;

/**
 * Instruction Constraints
 *
 * Given instruction, return instruction name
 * Created by MForever78 on 15/10/8.
 */

enum INST {
    /* R-type */
    ADD, SUB, AND, OR, XOR, NOR, SLT, SLL, SRL, SRA, SLLV, SRLV, SRAV, JR,
    /* I-type */
    ADDI, ADDIU, ANDI, ORI, XORI, LUI, LW, SW, BEQ, BNE, SLTI,
    /* J-type */
    J, JAL,
    /* ERROR */
    ERROR
}

public class Instructions {
    static INST getInstName(int inst) {
        int opcode = inst >> 26;
        int funct = inst << 26 >>> 26;
        System.out.println("funct: " + funct);
        switch (opcode) {
            case 0b000000:
                switch (funct) {
                    case 0b100000:
                        return INST.ADD;
                    case 0b100010:
                        return INST.SUB;
                    case 0b100100:
                        return INST.AND;
                    case 0b100101:
                        return INST.OR;
                    case 0b100110:
                        return INST.XOR;
                    case 0b100111:
                        return INST.NOR;
                    case 0b101010:
                        return INST.SLT;
                    case 0b000000:
                        return INST.SLL;
                    case 0b000010:
                        return INST.SRL;
                    case 0b000011:
                        return INST.SRA;
                    case 0b000100:
                        return INST.SLLV;
                    case 0b000110:
                        return INST.SRLV;
                    case 0b000111:
                        return INST.SRAV;
                    case 0b001000:
                        return INST.JR;
                    default:
                        return INST.ERROR;
                }
            case 0b001000:
                return INST.ADDI;
            case 0b001001:
                return INST.ADDIU;
            case 0b001100:
                return INST.ANDI;
            case 0b001101:
                return INST.ORI;
            case 0b001110:
                return INST.XORI;
            case 0b001111:
                return INST.LUI;
            case 0b100011:
                return INST.LW;
            case 0b101011:
                return INST.SW;
            case 0b000100:
                return INST.BEQ;
            case 0b000101:
                return INST.BNE;
            case 0b001010:
                return INST.SLTI;
            case 0b000010:
                return INST.J;
            case 0b000011:
                return INST.JAL;
            default:
                return INST.ERROR;
        }
    }
}
