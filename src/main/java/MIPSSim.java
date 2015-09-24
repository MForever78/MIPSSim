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
    public static int opcode, rs, rt, rd, immediate, funct, address, shamt, signExtImm, sign;
    public static final int ra = 31;

    public static void printDebug() {
        System.out.printf("PC: 0x%8X\n", PC);
        System.out.printf("IR: 0x%8X\n", IR);
        System.out.println("opcode: " + Integer.toBinaryString(opcode));
        System.out.println("rs: " + rs);
        System.out.println("rt: " + rt);
        System.out.println("rd: " + rd);
        System.out.println("shamt: " + shamt);
        System.out.println("funct: " + funct);
        System.out.println("immediate: " + immediate);
        System.out.println("address: " + address);
        System.out.println();
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
            IR = memory[PC >> 2];
            PC = PC + 4;
            opcode = IR >> 26;
            rs = IR << 6 >> 27;
            rt = IR << 11 >> 27;
            rd = IR << 16 >> 27;
            shamt = IR << 21 >> 27;
            funct = IR << 26 >> 28;
            immediate = IR << 16 >> 16;
            signExtImm = (immediate & 0x00008000) == 0 ? immediate : immediate | 0xffff0000;
            address = IR << 6 >> 6;
            switch(opcode) {
                case 0b000000:
                    /* R-type instructions */
                    switch (funct) {
                        case 0b100000:                                  // add
                            Regs[rs] = Regs[rt] + Regs[rd];
                            break;
                        case 0b100010:                                  // sub
                            Regs[rs] = Regs[rt] - Regs[rd];
                            break;
                        case 0b100100:                                  // and
                            Regs[rs] = Regs[rt] & Regs[rd];
                            break;
                        case 0b100101:                                  // or
                            Regs[rs] = Regs[rt] | Regs[rd];
                            break;
                        case 0b100110:                                  // xor
                            Regs[rs] = Regs[rt] ^ Regs[rd];
                            break;
                        case 0b100111:                                  // nor
                            Regs[rs] = ~(Regs[rt] | Regs[rd]);
                            break;
                        case 0b101010:                                  // slt
                            Regs[rs] = Regs[rt] > Regs[rd] ? 1 : 0;
                            break;
                        case 0b000000:                                  // sll
                            Regs[rd] = Regs[rt] << shamt;
                            break;
                        case 0b000010:                                  // srl
                            Regs[rd] = Regs[rt] >> shamt;
                            break;
                        case 0b000011:                                  // sra
                            sign = Regs[rt] & 0x80000000;
                            Regs[rd] = Regs[rt] & 0x7fffffff >> shamt | sign;
                            break;
                        case 0b000100:                                  // sllv
                            Regs[rs] = Regs[rt] << Regs[rd];
                            break;
                        case 0b000110:                                  // srlv
                            Regs[rs] = Regs[rt] >> Regs[rd];
                            break;
                        case 0b000111:                                  // srav
                            sign = Regs[rt] & 0x80000000;
                            Regs[rs] = Regs[rt] & 0x7fffffff >> Regs[rd] | sign;
                            break;
                        case 0b001000:                                  // jr
                            PC = Regs[rs];
                            break;
                        default:
                            break;
                    }
                    break;
                /* I-type instructions */
                case 0b001000:
                    Regs[rs] = Regs[rt] + signExtImm;                   // addi
                    break;
                case 0b001001:
                    Regs[rs] = Regs[rt] + immediate;                    // addiu
                    break;
                case 0b001100:
                    Regs[rs] = Regs[rt] & immediate;                    // andi
                    break;
                case 0b001101:
                    Regs[rs] = Regs[rt] | immediate;                    // ori
                    break;
                case 0b001110:
                    Regs[rs] = Regs[rt] ^ immediate;                    // xori
                    break;
                case 0b001111:
                    Regs[rt] = immediate << 16;                         // lui
                    break;
                case 0b100011:
                    Regs[rt] = memory[Regs[rs] + signExtImm];           // lw
                    break;
                case 0b101011:
                    memory[Regs[rs] + signExtImm] = Regs[rt];           //sw
                    break;
                case 0b000100:
                    if (Regs[rs] == Regs[rt])                           // beq
                        PC += immediate >> 2;
                    break;
                case 0b000101:
                    if (Regs[rs] != Regs[rt])                           // bne
                        PC -= immediate >> 2;
                    break;
                case 0b001010:
                    Regs[rt] = Regs[rs] < signExtImm ? 1 : 0;           // slti
                    break;
                /* J-type instructions */
                case 0b000010:
                    PC = address;                                       // j
                    break;
                case 0b000011:
                    Regs[ra] = PC;                                      // jal
                    PC = address;
                    break;
                default:
                    break;
            }
            printDebug();
            sc.nextLine();
        }
    }
}
