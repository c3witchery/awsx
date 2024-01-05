import com.pulumi.Context
import infra.defaultVpcStack
//import infra.kubernetesStack
import infra.vpcStack
//import infra.JumpServerCreation

fun main(args: Array<String>) {

    com.pulumi.Pulumi.run { ctx ->

        //defaultVpcStack(ctx)
        vpcStack(ctx)
//       JumpServerCreation(ctx)
//       kubernetesStack(ctx)
    }



}