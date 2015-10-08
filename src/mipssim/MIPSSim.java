package mipssim;

import java.util.Scanner;

/**
 * MIPSSim
 *
 * A MIPS simulator
 * Created by MForever78 on 15/9/22.
 */

public class MIPSSim {
    public static int memory[];
    public static int[] Regs = new int[32];
    public static int PC;
    public static int IR;
    public static int rs, rt, rd, immediate, address, shamt, signExtImm, sign;
    public static INST instName;
    public static String instruction;
    public static final int ra = 31;

    public static void printDebug() {
        System.out.printf("PC: 0x%8X\n", PC);
        System.out.println(instruction);
    }

    public static void main(String[] args) {
        PC = 0;
        memory = new int[] {
                0x00004020,
                0x21080001,
                0x01084820,
                0x01295020,
                0x014a5820,
                0x016a6022,
                0x118b0002,
                0x018b6020,
                0x08000000,
                0x08000009
        };
        Scanner sc = new Scanner(System.in);

        while (PC >> 2 < memory.length) {
            IR = memory[PC >>> 2];
            PC = PC + 4;
            rs = IR << 6 >>> 27;
            rt = IR << 11 >>> 27;
            rd = IR << 16 >>> 27;
            shamt = IR << 21 >>> 27;
            immediate = IR << 16 >>> 16;
            signExtImm = (immediate & 0x00008000) == 0 ? immediate : immediate | 0xffff0000;
            address = IR << 6 >>> 6;
            instName = Instructions.getInstName(IR);
            instruction = Instructions.disassemble(IR);
            switch(instName) {
                case ADD:   Regs[rs] = Regs[rt] + Regs[rd];             break;
                case SUB:   Regs[rs] = Regs[rt] - Regs[rd];             break;
                case AND:   Regs[rs] = Regs[rt] & Regs[rd];             break;
                case OR:    Regs[rs] = Regs[rt] | Regs[rd];             break;
                case XOR:   Regs[rs] = Regs[rt] ^ Regs[rd];             break;
                case NOR:   Regs[rs] = ~(Regs[rt] | Regs[rd]);          break;
                case SLT:   Regs[rs] = Regs[rt] > Regs[rd] ? 1 : 0;     break;
                case SLL:   Regs[rd] = Regs[rt] << shamt;               break;
                case SRL:   Regs[rd] = Regs[rt] >> shamt;               break;

                case SRA:
                    sign = Regs[rt] & 0x80000000;
                    Regs[rd] = Regs[rt] & 0x7fffffff >> shamt | sign;
                    break;

                case SLLV:  Regs[rs] = Regs[rt] << Regs[rd];            break;
                case SRLV:  Regs[rs] = Regs[rt] >> Regs[rd];            break;

                case SRAV:
                    sign = Regs[rt] & 0x80000000;
                    Regs[rs] = Regs[rt] & 0x7fffffff >> Regs[rd] | sign;
                    break;

                case JR:    PC = Regs[rs];                              break;
                case ADDI:  Regs[rs] = Regs[rt] + signExtImm;           break;
                case ADDIU: Regs[rs] = Regs[rt] + immediate;            break;
                case ANDI:  Regs[rs] = Regs[rt] & immediate;            break;
                case ORI:   Regs[rs] = Regs[rt] | immediate;            break;
                case XORI:  Regs[rs] = Regs[rt] ^ immediate;            break;
                case LUI:   Regs[rt] = immediate << 16;                 break;
                case LW:    Regs[rt] = memory[Regs[rs] + signExtImm];   break;
                case SW:    memory[Regs[rs] + signExtImm] = Regs[rt];   break;

                case BEQ:
                    if (Regs[rs] == Regs[rt])
                        PC += immediate >> 2;
                    break;

                case BNE:
                    if (Regs[rs] != Regs[rt])
                        PC -= immediate >> 2;
                    break;

                case SLTI:  Regs[rt] = Regs[rs] < signExtImm ? 1 : 0;   break;
                case J:     PC = address;                               break;
                case JAL:   Regs[ra] = PC;  PC = address;               break;
                default:                                                break;
            }
            printDebug();
            sc.nextLine();
        }
    }
}
