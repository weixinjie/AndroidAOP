package will.github.com.traceplugin;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AdviceAdapter;

/**
 * 方法耗时 visitor
 * Author: lwh
 * Date: 4/28/17 15:37.
 */

public class TraceVisitor extends ClassVisitor {

    private String className;
    private String superName;
    private String[] interfaces;

    public TraceVisitor(String className, ClassVisitor classVisitor) {
        super(Opcodes.ASM5, classVisitor);
    }

    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature,
                                     String[] exceptions) {
        MethodVisitor methodVisitor = cv.visitMethod(access, name, desc, signature, exceptions);
        methodVisitor = new AdviceAdapter(Opcodes.ASM5, methodVisitor, access, name, desc) {


            private TraceType isInject() {
                if (!className.contains("1905") || className.contains("BaseActivity")) {
                    return TraceType.NULL;
                }
                if (name.contains("onClick") && desc.equals("(Landroid/view/View;)V")) {
                    return TraceType.ONCLICK;
                } else if (superName.contains("Activity")) {
                    return TraceType.ACTIVITY;
                }
                return TraceType.NULL;
            }

            @Override
            public void visitCode() {
                super.visitCode();

            }

            @Override
            public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
                return super.visitAnnotation(desc, visible);
            }

            @Override
            public void visitFieldInsn(int opcode, String owner, String name, String desc) {
                super.visitFieldInsn(opcode, owner, name, desc);
            }


            @Override
            protected void onMethodEnter() {
                //统计方法耗时
                switch (isInject()) {
                    case NULL:
                        break;
                    case ONCLICK:
                        mv.visitVarInsn(ALOAD, 1);
                        mv.visitMethodInsn(INVOKESTATIC
                                , "com/m1905/mobilefree/trace/TraceUtil"
                                , "onViewClick"
                                , "(Landroid/view/View;)Z"
                                , false);
                        Label l1 = new Label();
                        mv.visitJumpInsn(IFNE, l1);
                        mv.visitInsn(RETURN);
                        mv.visitLabel(l1);
                        break;
                    case ACTIVITY:
                        if (name.equals("onDestroy")) {
                            mv.visitVarInsn(ALOAD, 0);
                            mv.visitMethodInsn(INVOKESTATIC, "com/m1905/mobilefree/trace/TraceUtil", "onActivityDestroy", "(Landroid/app/Activity;)V", false);
                        } else if (name.equals("onCreate")) {
                            System.out.println("执行onCreate方法" + name + desc + superName + className);
                            mv.visitVarInsn(ALOAD, 0);
                            mv.visitMethodInsn(INVOKESTATIC, "com/m1905/mobilefree/trace/TraceUtil", "onActivityCreate", "(Landroid/app/Activity;)V", false);
                        }
                        break;
                    case FRAGMENT:
                        break;
                }
            }

            @Override
            protected void onMethodExit(int i) {
                super.onMethodExit(i);
            }
        };
        return methodVisitor;
        //return super.visitMethod(i, s, s1, s2, strings);

    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        this.className = name;
        this.superName = superName;
        this.interfaces = interfaces;
//        System.out.println("类名  " + className + "  父类名: " + superName);
//        for (int i = 0; i < interfaces.length; i++) {
//            System.out.println("类名  " + className + "  实现的接口名: " + interfaces[i]);
//        }
    }
}
