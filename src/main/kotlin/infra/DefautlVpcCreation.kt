package infra

import com.pulumi.Context
import com.pulumi.awsx.ec2.VpcArgs
//import com.pulumi.aws.AwsFunctions
//import com.pulumi.aws.cloudhsmv2.Cluster
//import com.pulumi.aws.cloudhsmv2.ClusterArgs
//import com.pulumi.aws.cloudhsmv2.Hsm
//import com.pulumi.aws.cloudhsmv2.HsmArgs
//import com.pulumi.aws.ec2.*
//import com.pulumi.aws.ec2.enums.InstanceType
//import com.pulumi.aws.ec2.inputs.GetAmiArgs
//import com.pulumi.aws.ec2.inputs.GetAmiFilterArgs
//import com.pulumi.aws.ec2.inputs.SecurityGroupEgressArgs
//import com.pulumi.aws.ec2.inputs.SecurityGroupIngressArgs
//import com.pulumi.aws.iam.Role
//import com.pulumi.aws.iam.RoleArgs
//import com.pulumi.aws.iam.RolePolicyAttachment
//import com.pulumi.aws.iam.RolePolicyAttachmentArgs
//import com.pulumi.aws.inputs.GetAvailabilityZonesArgs

import com.pulumi.awsx.ec2.Vpc;
//import com.pulumi.core.Output
//import com.pulumi.resources.CustomResourceOptions

fun defaultVpcStack(ctx: Context) {


//    val availabilityZonesOutputs = AwsFunctions.getAvailabilityZones(
//        GetAvailabilityZonesArgs.builder().build()
//    )


    val defaultVpc = Vpc(
        "vodafoneProdVpc"
        ,
        VpcArgs.builder()
            .numberOfAvailabilityZones(3).tags(mapOf("Owner" to "cesare.urso@r3.com", "Name" to "vodafone-eks-role"))
            .build()
            )


    ctx.export("vpcId", defaultVpc.vpcId())
//    ctx.export("privateSubnetIds", defaultVpc.privateSubnetIds())
//    ctx.export("publicSubnetIds", defaultVpc.publicSubnetIds())

//    //Create Security Group
//    val ec2AllowRule = SecurityGroup(
//        "ec2AllowRule", SecurityGroupArgs.builder()
//            .vpcId(defaultVpc.vpcId())
//            .ingress(
//                SecurityGroupIngressArgs.builder()
//                    .description("ANY")
//                    .fromPort(0)
//                    .toPort(0)
//                    .protocol("-1")
//                    .cidrBlocks("0.0.0.0/0")
//                    .build()
//                ,
//                SecurityGroupIngressArgs.builder()
//                    .protocol("tcp")
//                    .fromPort(2225)
//                    .toPort(2225)
//                    .cidrBlocks("0.0.0.0/0").build()
//                ,
//                SecurityGroupIngressArgs.builder()
//                    .protocol("tcp")
//                    .fromPort(2223)
//                    .toPort(2223)
//                    .cidrBlocks("0.0.0.0/0").build()
//                ,
//                SecurityGroupIngressArgs.builder()
//                    .description("HTTPS")
//                    .fromPort(443)
//                    .toPort(443)
//                    .protocol("tcp")
//                    .cidrBlocks("0.0.0.0/0")
//                    .build()
//                ,
//                SecurityGroupIngressArgs.builder()
//                    .description("HTTP")
//                    .fromPort(80)
//                    .toPort(80)
//                    .protocol("tcp")
//                    .cidrBlocks("0.0.0.0/0")
//                    .build(),
////                SecurityGroupIngressArgs.builder()
////                    .description("MYSQL/Aurora")
////                    .fromPort(3306)
////                    .toPort(3306)
////                    .protocol("tcp")
////                    .cidrBlocks("0.0.0.0/0")
////                    .build(),
//                SecurityGroupIngressArgs.builder()
//                    .description("SSH")
//                    .fromPort(22)
//                    .toPort(22)
//                    .protocol("tcp")
//                    .cidrBlocks("0.0.0.0/0")
//                    .build()
//            )
//            .egress(
//                SecurityGroupEgressArgs.builder()
//                    .fromPort(0)
//                    .toPort(0)
//                    .protocol("-1")
//                    .cidrBlocks("0.0.0.0/0")
//                    .build()
//            )
//            .tags(mapOf("Owner" to "cesare.urso@r3.com", "Name" to " Security Group that allows ssh,http,https"))
//            .build()
//    )
//
//    // Create an IAM role for the EKS cluster
//    val eksRole = Role(
//        "eks-role",
//        RoleArgs.builder()
//            .assumeRolePolicy("{\"Version\": \"2012-10-17\",\"Statement\": [{\"Effect\": \"Allow\",\"Principal\": {\"Service\": \"eks.amazonaws.com\"},\"Action\": \"sts:AssumeRole\"}]}")
//            .tags(mapOf("Owner" to "cesare.urso@r3.com", "Name" to "vodafone-eks-role"))
//            .build()
//    )
//    ctx.export("eksRole", eksRole.name())
//
//    // Attach the AmazonEKSClusterPolicy to the created role
//    val eksClusterPolicyAttachment = RolePolicyAttachment(
//        "eks-cluster-policy-attachment",
//        RolePolicyAttachmentArgs.builder()
//            .role(eksRole.name())
//            .policyArn("arn:aws:iam::aws:policy/AmazonEKSClusterPolicy")
//            .build()
//    )
//    ctx.export("eksClusterPolicyAttachment", eksClusterPolicyAttachment.id())
//
//    // Attach the AmazonEKSServicePolicy to the created role
//    val eksServicePolicyAttachment = RolePolicyAttachment(
//        "eks-service-policy-attachment",
//        RolePolicyAttachmentArgs.builder()
//            .role(eksRole.name())
//            .policyArn("arn:aws:iam::aws:policy/AmazonEKSServicePolicy")
//            .build()
//    )
//
////    // Create an EKS cluster attached to the created VPC and Subnet
////    val eksCluster = Cluster(
////            "eks-cluster",
////            ClusterArgs.builder()
////                .roleArn(eksRole.arn())
////                .vpcConfig(
////                    ClusterVpcConfigArgs.builder()
////                        //.vpcId(vpc.id())
////                        .subnetIds(
////                            Output.all(prodSubnetPrivate1.id(),prodSubnetPrivate2.id())
////                        )
////                        .build()
////                )
////            .tags(mapOf("Owner" to "cesare.urso@r3.com", "Name" to "vodafone-eks-cluster"))
////            .build()
////            ,
////            CustomResourceOptions.builder()
////                .dependsOn(listOf(prodSubnetPrivate1, prodSubnetPrivate2, vpc, eksRole, eksClusterPolicyAttachment, eksServicePolicyAttachment))
////                .build()
////        )
////        ctx.export("eksCluster.clusterId", eksCluster.clusterId())
////        ctx.export("eksClusterName", eksCluster.name())
////
////        // Define subnet IDs for the CloudHSM cluster
////    val cloudHsmSubnetIds = Output.all(prodSubnetPrivate1.id(),prodSubnetPrivate2.id()).applyValue { ids -> listOf(ids) }
////        ctx.export("cloudHsmSubnetIds", cloudHsmSubnetIds)
////
//    // Create a CloudHSM v2 cluster in the specified subnet
//    val cloudHsmCluster = Cluster(
//        "cloudhsm-cluster", ClusterArgs.builder()
//            .hsmType("hsm1.medium")
//            .subnetIds(
//               defaultVpc.publicSubnetIds()
//            )
//            .tags(mapOf("Owner" to "cesare.urso@r3.com", "Name" to "vodafone-cloudhsm-cluster"))
//            .build()
//    )
//    ctx.export("cloudHsmClusterId", cloudHsmCluster.clusterId())
//
//    // Create the First HSM instance (Hardware Security Module)
//    val hsm1 = Hsm(
//        "vodafone-hsm1", HsmArgs.builder()
//            .clusterId(cloudHsmCluster.id())
//            .subnetId(defaultVpc.privateSubnetIds().applyValue{it -> it[0]})
//            .build(),
//            CustomResourceOptions.builder()
//            .dependsOn(listOf(defaultVpc))
//            .build()
//        )
//        ctx.export("hsm1Id", hsm1.id())
//        ctx.export("hsm1IpAddress", hsm1.ipAddress())
//
//    // Create the second HMS
//    val hsm2 = Hsm(
//        "vodafone-hsm2", HsmArgs.builder()
//            .clusterId(cloudHsmCluster.id())
//            .subnetId(defaultVpc.privateSubnetIds().applyValue{it -> it[1]})
//            .build(),
//        CustomResourceOptions.builder()
//            .dependsOn(listOf( defaultVpc))
//            .build()
//    )
//    ctx.export("hsm2Id", hsm2.id())
//    ctx.export("hsm2IpAddress", hsm2.ipAddress())
//
////    Cloud init happen with the cloud-hsm-init project
////    Create a local machine where to run the commands needed for the Cloud HSM initialisation
//    val awsLinuxAmi = Ec2Functions.getAmi(
//        GetAmiArgs.builder()
//            .owners("amazon")
//            .filters(
//                GetAmiFilterArgs.builder()
//                    .name("name")
//                    .values("amzn2-ami-hvm-*-x86_64-ebs")
//                    .build()
//            )
//            .mostRecent(true)
//            .tags(mapOf("Owner" to "cesare.urso@r3.com", "Name" to "vodafone-awsLinuxAmi"))
//            .build()
//    ).applyValue{result -> result.id()}
//    val securityGroupJumpServer = SecurityGroup(
//        "vodafone-jump-server-secgrp", SecurityGroupArgs.builder()
//            .description("jump server for CloudHSM activation")
//            .vpcId(defaultVpc.vpcId())
//            .ingress(
//                listOf(
//                    SecurityGroupIngressArgs.builder()
//                        .protocol("tcp")
//                        .fromPort(22)
//                        .toPort(22)
//                        .cidrBlocks("0.0.0.0/0").build()
//                    ,
//                    SecurityGroupIngressArgs.builder()
//                        .protocol("tcp")
//                        .fromPort(2225)
//                        .toPort(2225)
//                        .cidrBlocks("0.0.0.0/0").build()
//                    ,
//                    SecurityGroupIngressArgs.builder()
//                        .protocol("tcp")
//                        .fromPort(2223)
//                        .toPort(2223)
//                        .cidrBlocks("0.0.0.0/0").build()
//                    ,
//                    SecurityGroupIngressArgs.builder()
//                        .description("ANY")
//                        .fromPort(0)
//                        .toPort(0)
//                        .protocol("-1")
//                        .cidrBlocks("0.0.0.0/0")
//                        .build()
//                    ,
//                    SecurityGroupIngressArgs.builder()
//                        .description("HTTPS")
//                        .fromPort(443)
//                        .toPort(443)
//                        .protocol("tcp")
//                        .cidrBlocks("0.0.0.0/0")
//                        .build()
//                    ,
//                    SecurityGroupIngressArgs.builder()
//                        .description("HTTP")
//                        .fromPort(80)
//                        .toPort(80)
//                        .protocol("tcp")
//                        .cidrBlocks("0.0.0.0/0")
//                        .build())
//            ).egress(
//                listOf(
//                    SecurityGroupEgressArgs.builder()
//                        .protocol("-1")
//                        .fromPort(0)
//                        .toPort(0)
//                        .cidrBlocks("0.0.0.0/0")
//                        .build())
//            )
//            .build()
//    )
//    val keyPair = KeyPair("ec2-keypair", KeyPairArgs.builder()
//        .keyName("jump-server-key-pair")
//        .publicKey("ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABgQDKgqvp258z+UBTaq77A04wgg/ipL5nvj2lc421SQVLvzoLeZHo0rnFlRrrIIZ7wog7Qe7AjThygbmVHw5gHm+L8XjLeVlsxRS77NjkqWylkanAAbVO1hiZpTpT8bcDLOxTWQWnL5A2JP3r8ZgW4zzNNrTjA8GeRDaQph3Y3NBREDsq4WBghykK8NRHxeEDAJfDcVa3TuWQE+23quZKZXfgcyzgRY3icHZiPzDI0Wmr0QRaXSr9xAGU1EgmP/vV7QSr8hRWRHv9nNWwAJGjRueBM0XZVveukiAGlX1EvH00rL+76CuX4RhM+3UHNgWmfEv8O8c3ZZ++MqsY9UhrNbszMRVWMe9WcV4hryZ+xLLT/20Xl39VoxWGXtQWFebAMUsSeVKG4ZM+c0lMJPLFtxxkiAinGONtY3T1kEzOTwcOgtjAIGwRLe4R5OyjBiyj0XNNSOiRI0YNpkTbOfScDqlVDRmCwBSvGmI2RzwH42QU2AwP7i14sgFzrLVZ/HKpIxM= cesare.urso@LDNM-RC27HGD6XV")
//        .build())
//    ctx.export("keyPairName", keyPair.keyName())
//
//    val jumpServer = Instance(
//        "vodafone-jump-server", InstanceArgs.builder()
//            .tags(mapOf("Owner" to "cesare.urso@r3.com",  "Name" to "vodafone-jump-server"))
//            .instanceType(Output.ofRight(InstanceType.T2_Micro))
//            .securityGroups(Output.all(securityGroupJumpServer.id()))
//            .subnetId(defaultVpc.publicSubnetIds().applyValue{it -> it[0]})
//            .availabilityZone(
//                Output.all(availabilityZonesOutputs).applyValue { it ->
//                    it.last().names().get(0) }
//            )
//            .associatePublicIpAddress(true)
//            .keyName(keyPair.keyName())
//            .ami(awsLinuxAmi)
////            .userData(
////                        """
////                            #!/bin/bash
////                            curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
////                            unzip awscliv2.zip
////                            sudo ./aws/install
////                            aws --version
////                         """
////            )
//            .tags(mapOf("Owner" to "cesare.urso@r3.com", "Name" to "vodafone-jump-server"))
//            .build(),
//        CustomResourceOptions.builder()
//            .dependsOn(listOf( defaultVpc, keyPair))
//            .build()
//    )
//    ctx.export("jumpServerPublicIp", jumpServer.publicIp())
//    ctx.export("jumpServerId", jumpServer.id())
//    ctx.export("jumpServerPublicDns", jumpServer.publicDns())

//   //Create the KMS for Cloud HSM for local AWS account PS 450694575897 (to be replaced for dev  vs Client deployment)
////    val kmsKey = Key(
////        "kms-key-for-cloudhsm",
////        KeyArgs.builder()
////            .customKeyStoreId(cloudHsmCluster.id())
////            .description("KMS Key backed by CloudHSM Cluster")
////            .policy(
////                cloudHsmCluster.hsmType().applyValue { hsmType ->
////                    """
////                   {
////  "Version": "2012-10-17",
////  "Id": "key-default-1",
////  "Statement": [
////    {
////      "Sid": "Enable IAM policies",
////      "Effect": "Allow",
////      "Principal": {
////        "AWS": "arn:aws:iam::450694575897:root"
////      },
////      "Action": "kms:*",
////      "Resource": "*"
////    },
////    {
////                                "Sid": "Allow administration of the key",
////                                "Effect": "Allow",
////                                "Principal": {
////                                    "AWS": [
////                                        "arn:aws:iam::450694575897:role/admin"
////                                    ]
////                                },
////                                "Action": [
////                                    "kms:Create*",
////                                    "kms:Describe*",
////                                    "kms:Enable*",
////                                    "kms:List*",
////                                    "kms:Put*",
////                                    "kms:Update*",
////                                    "kms:Revoke*",
////                                    "kms:Disable*",
////                                    "kms:Get*",
////                                    "kms:Delete*",
////                                    "kms:ScheduleKeyDeletion",
////                                    "kms:CancelKeyDeletion"
////                                ],
////                                "Resource": "*"
////                            }
////  ]
////}
////                    """.trimIndent()
////            })
////
////            .build()
////    )
////    ctx.export("kmsKeyArn", kmsKey.arn())


}


