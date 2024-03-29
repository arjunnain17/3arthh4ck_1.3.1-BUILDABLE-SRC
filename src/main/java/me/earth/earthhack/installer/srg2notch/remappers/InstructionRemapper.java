package me.earth.earthhack.installer.srg2notch.remappers;

import me.earth.earthhack.installer.srg2notch.Mapping;
import me.earth.earthhack.installer.srg2notch.MappingUtil;
import me.earth.earthhack.installer.srg2notch.remappers.Remapper;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.InvokeDynamicInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.MultiANewArrayInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;

public class InstructionRemapper
implements Remapper {
    @Override
    public void remap(ClassNode cn, Mapping mapping) {
        for (MethodNode mn : cn.methods) {
            for (int i = 0; i < mn.instructions.size(); ++i) {
                AbstractInsnNode insn = mn.instructions.get(i);
                this.remapInsn(insn, mapping);
            }
        }
    }

    private void remapInsn(AbstractInsnNode insn, Mapping mapping) {
        if (insn instanceof MethodInsnNode) {
            MethodInsnNode min = (MethodInsnNode)insn;
            min.owner = mapping.getClasses().getOrDefault(min.owner, min.owner);
            min.desc = MappingUtil.mapDescription(min.desc, mapping);
            min.name = MappingUtil.map(min.owner, min.name, min.desc, mapping);
        } else if (insn instanceof FieldInsnNode) {
            FieldInsnNode f = (FieldInsnNode)insn;
            f.owner = mapping.getClasses().getOrDefault(f.owner, f.owner);
            f.desc = MappingUtil.mapDescription(f.desc, mapping);
            if (f.getOpcode() == 178 && !f.name.startsWith("field")) {
                String constant = f.owner + "/" + f.name;
                f.name = mapping.getConstants().getOrDefault(constant, f.name);
            } else {
                f.name = mapping.getFields().getOrDefault(f.name, f.name);
            }
        } else if (insn instanceof TypeInsnNode) {
            TypeInsnNode t = (TypeInsnNode)insn;
            t.desc = t.desc.contains(";") ? MappingUtil.mapDescription(t.desc, mapping) : mapping.getClasses().getOrDefault(t.desc, t.desc);
        } else if (insn instanceof MultiANewArrayInsnNode) {
            MultiANewArrayInsnNode mainsn = (MultiANewArrayInsnNode)insn;
            mainsn.desc = MappingUtil.mapDescription(mainsn.desc, mapping);
        } else if (insn instanceof InvokeDynamicInsnNode) {
            InvokeDynamicInsnNode dyn = (InvokeDynamicInsnNode)insn;
            dyn.desc = MappingUtil.mapDescription(dyn.desc, mapping);
            dyn.name = MappingUtil.map(null, dyn.name, dyn.desc, mapping);
            dyn.bsm = MappingUtil.map(dyn.bsm, mapping);
            if (dyn.bsmArgs != null) {
                Object[] args = new Object[dyn.bsmArgs.length];
                for (int i = 0; i < dyn.bsmArgs.length; ++i) {
                    Object arg = dyn.bsmArgs[i];
                    if (arg == null) {
                        args[i] = null;
                        continue;
                    }
                    if (arg instanceof Type) {
                        arg = MappingUtil.map((Type)arg, mapping);
                    } else if (arg instanceof Handle) {
                        arg = MappingUtil.map((Handle)arg, mapping);
                    } else {
                        throw new ClassCastException("InvokeDynamic Arg " + arg.getClass().getName() + " : " + arg + " was not a Handle or a Type!");
                    }
                    args[i] = arg;
                }
                dyn.bsmArgs = args;
            }
        } else if (insn instanceof LdcInsnNode) {
            LdcInsnNode ldc = (LdcInsnNode)insn;
            if (ldc.cst instanceof Type) {
                ldc.cst = MappingUtil.map((Type)ldc.cst, mapping);
            }
        } else if (insn instanceof FrameNode) {
            FrameNode frame = (FrameNode)insn;
            frame.local = MappingUtil.map(frame.local, mapping);
            frame.stack = MappingUtil.map(frame.stack, mapping);
        }
    }
}
