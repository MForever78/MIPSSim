package mipssim;

import java.util.Arrays;

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
    ADDI, ADDIU, ANDI, ORI, XORI, LUI, LW, SW, LH, SH, LB, SB, BEQ, BNE, SLTI,
    /* J-type */
    J, JAL,
    /* ERROR */
    ERROR
}

public class Instructions {
    static INST getInstName(int inst) {
        int opcode = inst >> 26;
        int funct = inst << 26 >>> 26;
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

    static String disassemble(int inst) {
         String[] regNameMap = {
            "$zero", "$at", "$v0", "$v1", "$a0", "$a1", "$a2", "$a3",
            "$t0", "$t1", "$t2", "$t3", "$t4", "$t5", "$t6", "$t7",
            "$s0", "$s1", "$s2", "$s3", "$s4", "$s5", "$s6", "$s7",
            "$t8", "$t9",
            "$k0", "$k1",
            "$gp", "$sp", "$fp", "$ra"
        };
        INST[] RType = {
            INST.ADD, INST.SUB, INST.AND, INST.OR, INST.XOR, INST.NOR,
            INST.SLLV, INST.SRLV, INST.SRAV
        };
        INST[] IType = {
            INST.ADDI, INST.ADDIU, INST.ANDI, INST.ORI, INST.XORI,
            INST.BEQ, INST.BNE, INST.SLTI
        };
        INST[] SLType = {
            INST.SW, INST.SH, INST.SB, INST.LW, INST.LH, INST.LB
        };
        INST[] LogicType = {
            INST.SLL, INST.SRL, INST.SRA
        };
        String rs = regNameMap[inst << 6 >>> 27];
        String rt = regNameMap[inst << 11 >>> 27];
        String rd = regNameMap[inst << 16 >>> 27];
        int shamt = inst << 21 >>> 27;
        int immediate = inst << 16 >>> 16;
        int signExtImm = (immediate & 0x00008000) == 0 ? immediate : immediate | 0xffff0000;
        int address = inst << 6 >>> 6;
        INST instName = Instructions.getInstName(inst);

        if (Arrays.asList(RType).contains(instName)) {
            return String.format("%s\t%s, %s, %s", instName, rd, rs, rt);
        } else if (Arrays.asList(IType).contains(instName)) {
            return String.format("%s\t%s, %s, %d", instName, rt, rs, signExtImm);
        } else if (Arrays.asList(SLType).contains(instName)) {
            return String.format("%s\t%s, %d(%s)", instName, rt, signExtImm, rs);
        } else if (Arrays.asList(LogicType).contains(instName)) {
            return String.format("%s\t%s, %s, %d", instName, rd, rt, shamt);
        }

        switch (instName) {
            // Non-standard instructions
            case JR:    return String.format("JR\t%s", rs);
            case LUI:   return String.format("LUI\t%s, 0x%x", rt, immediate);
            case J:     return String.format("J\t\t0x%x", address);
            case JAL:   return String.format("JAL\t0x%x", address);
        }

        return "ERROR";
    }
}
