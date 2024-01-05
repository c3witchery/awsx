package infra

import com.pulumi.Context
import com.pulumi.aws.ec2.Instance
import com.pulumi.aws.ec2.Ec2Functions
import com.pulumi.aws.ec2.InstanceArgs
import com.pulumi.aws.ec2.SecurityGroup
import com.pulumi.aws.ec2.SecurityGroupArgs
import com.pulumi.aws.ec2.enums.InstanceType
import com.pulumi.aws.ec2.inputs.GetAmiArgs
import com.pulumi.aws.ec2.inputs.GetAmiFilterArgs
import com.pulumi.aws.ec2.inputs.GetVpcArgs
import com.pulumi.aws.ec2.inputs.SecurityGroupIngressArgs
import com.pulumi.core.Output
import com.pulumi.resources.CustomResourceOptions
import com.pulumi.resources.StackReference

fun JumpServerCreation (ctx : Context) {


//    val vpcCreationStackRef = StackReference(
//            "cesare/aws-cloudhsm-kubernetes-c4-deployment/aws-cloudhsm-kubernetes-c4-deployment"
//            )
//    val vpcId =
//        vpcCreationStackRef.output("vpcId")
//    ctx.export("importedVpcId",vpcId)
//    val prodSubnetPublic1Id = vpcCreationStackRef.output( "odafoneSubnetPublic1.id")
//    ctx.export("importedVpcId", prodSubnetPublic1Id)
//
//
//    //TODO Now obsolete  to remove if the entire pipeline works
//    //Create a local machine where to run the commands needed for the Cloud HSM initialisation
//    val awsLinuxAmi = Ec2Functions.getAmi(
//        GetAmiArgs.builder()
//            .owners("amazon")
//            .filters(
//                GetAmiFilterArgs.builder()
//                    .name("name")
//                    .values("amzn2-ami-hvm-*-x86_64-ebs")
//                    .build()
//            )
//        .mostRecent(true)
//        .tags(mapOf("Owner" to "cesare.urso@r3.com", "Name" to "vodafone-awsLinuxAmi"))
//        .build()
//        ).applyValue{result -> result.id()}
//    val securityGroupJumpServer = SecurityGroup(
//        "vodafone-jump-server-secgrp", SecurityGroupArgs.builder()
//            .description("Enable SSH access")
//            //.vpcId(vpc.id())
//            .ingress(
//                listOf(
//                SecurityGroupIngressArgs.builder()
//                .protocol("tcp")
//                .fromPort(22)
//                .toPort(22)
//                .cidrBlocks("0.0.0.0/0").build()
//            ))
//            .build()
//    )
//    val jumpServer = Instance(
//        "vodafone-jump-server", InstanceArgs.builder()
//            .tags(mapOf("Owner" to "cesare.urso@r3.com",  "Name" to "vodafone-jump-server"))
//            .instanceType(Output.ofRight(InstanceType.T2_Micro))
//            .securityGroups(Output.all(securityGroupJumpServer.id()))
//            .subnetId(prodSubnetPublic1Id as Output<String>)
//            .associatePublicIpAddress(true)
////            .networkInterfaces(
////                InstanceNetworkInterfaceArgs.builder().
////                    networkInterfaceId ( vpc.id()
////                    )
////                    .deviceIndex(0)
////                .build()
////            )
//            .ami(awsLinuxAmi)
//            .userData("#!/bin/bash\\n\" +  // User data for setting up the EC2 instance\n" +
//                    "                      \"sudo yum update -y\\n\" + \n" +
//                    "                      \"sudo yum install -y aws-cli\\n\" +\n" +
//                    "                      \"echo 'Jump server ready'\"")
//            .tags(mapOf("Owner" to "cesare.urso@r3.com", "Name" to "vodafone-jump-server"))
//            .build(),
//            CustomResourceOptions.builder()
//                .dependsOn(listOf(securityGroupJumpServer))
//            .build()
//    )
//    ctx.export("jumpServerPublicIp", jumpServer.publicIp())
//    ctx.export("jumpServerId", jumpServer.id())
//



}