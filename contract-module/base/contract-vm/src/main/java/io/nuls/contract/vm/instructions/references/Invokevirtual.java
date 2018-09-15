package io.nuls.contract.vm.instructions.references;

import io.nuls.contract.vm.Frame;
import io.nuls.contract.vm.MethodArgs;
import io.nuls.contract.vm.ObjectRef;
import io.nuls.contract.vm.Result;
import io.nuls.contract.vm.code.MethodCode;
import io.nuls.contract.vm.code.VariableType;
import io.nuls.contract.vm.natives.NativeMethod;
import io.nuls.contract.vm.util.Constants;
import org.objectweb.asm.tree.MethodInsnNode;

import java.util.List;
import java.util.Objects;

public class Invokevirtual {

    public static void invokevirtual(Frame frame) {
        MethodInsnNode methodInsnNode = frame.methodInsnNode();
        String className = methodInsnNode.owner;
        String methodName = methodInsnNode.name;
        String methodDesc = methodInsnNode.desc;

        List<VariableType> variableTypes = VariableType.parseArgs(methodDesc);
        MethodArgs methodArgs = new MethodArgs(variableTypes, frame.operandStack, false);
        ObjectRef objectRef = methodArgs.objectRef;
        if (objectRef == null) {
            frame.throwNullPointerException();
            return;
        }

        String type = objectRef.getVariableType().getType();

        if (!Objects.equals(className, type)) {
            if (objectRef.getVariableType().isPrimitiveType()) {

            } else {
                className = type;
            }
        }

        if (objectRef.isArray() && Constants.TO_STRING_METHOD_NAME.equals(methodName) && Constants.TO_STRING_METHOD_DESC.equals(methodDesc)) {
            className = Constants.OBJECT_CLASS_NAME;
        }

        MethodCode methodCode = frame.methodArea.loadMethod(className, methodName, methodDesc);

        //Log.opcode(frame.getCurrentOpCode(), objectRef, methodName, methodDesc);

        Result result = NativeMethod.run(methodCode, methodArgs, frame);
        if (result != null) {
            return;
        }

        frame.vm.run(methodCode, methodArgs.frameArgs, true);
    }

}
