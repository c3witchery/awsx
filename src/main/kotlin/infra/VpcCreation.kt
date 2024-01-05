package infra

//import com.pulumi.aws.kms.CustomKeyStore
//import com.pulumi.aws.kms.CustomKeyStoreArgs

import com.pulumi.Context
import com.pulumi.aws.AwsFunctions
import com.pulumi.aws.cloudhsmv2.Hsm
import com.pulumi.aws.cloudhsmv2.HsmArgs
import com.pulumi.aws.ec2.*
import com.pulumi.aws.ec2.enums.InstanceType
import com.pulumi.aws.ec2.inputs.*
import com.pulumi.aws.eks.Cluster
import com.pulumi.aws.eks.ClusterArgs
import com.pulumi.aws.eks.inputs.ClusterVpcConfigArgs
import com.pulumi.aws.iam.Role
import com.pulumi.aws.iam.RoleArgs
import com.pulumi.aws.iam.RolePolicyAttachment
import com.pulumi.aws.iam.RolePolicyAttachmentArgs
import com.pulumi.aws.inputs.GetAvailabilityZonesArgs
import com.pulumi.aws.rds.SubnetGroup
import com.pulumi.aws.rds.SubnetGroupArgs
import com.pulumi.core.Output
import com.pulumi.resources.CustomResourceOptions
//import com.pulumi.aws.ec2.
import com.pulumi.aws.cloudhsmv2.Cluster as HsmCluster
import com.pulumi.aws.cloudhsmv2.ClusterArgs as HsmClusterArgs

import com.pulumi.aws.rds.Instance as RdsInstance
import com.pulumi.aws.rds.InstanceArgs as RdsInstanceArgs
import com.pulumi.resources.ComponentResource
import com.pulumi.resources.ComponentResourceOptions

fun vpcStack(ctx: Context) {

    val availabilityZonesOutputs = AwsFunctions.getAvailabilityZones(
            GetAvailabilityZonesArgs.builder().build()
    )

    // instead create DefaultVPC


    // Create a new VPC with a specified CIDR
    val vpc = Vpc(
        "eks-vpc", VpcArgs.builder()
            .cidrBlock("10.0.0.0/16")
            .enableDnsHostnames(true)
            .enableDnsSupport(true)
            .tags(mapOf("Owner" to "cesare.urso@r3.com", "Name" to "vodafone-eks-vpc"))
            .build(),
        CustomResourceOptions.builder()
            .dependsOn(listOf( ))
            .build()
    )
    ctx.export("vpcIp", vpc.id())

    val prodSubnetPublic1 = Subnet(
        "vodafoneSubnetPublic1", SubnetArgs.builder()
            .vpcId(vpc.id())
            .cidrBlock("10.0.128.0/20")
            .mapPublicIpOnLaunch(true)
            .availabilityZone(
                Output.all(availabilityZonesOutputs)
                    .applyValue {it ->
                        it.last().names().get(0)
                    }
            )
            .tags(mapOf("Owner" to "cesare.urso@r3.com", "Name" to "vodafone-eks-public1-subnet"))
            .build(),
        CustomResourceOptions.builder().parent(vpc).build()
    )
    ctx.export("prodSubnetPublic1Id", prodSubnetPublic1.id())

    val prodSubnetPublic2 = Subnet(
        "vodafoneSubnetPublic2", SubnetArgs.builder()
            .vpcId(vpc.id())
            .cidrBlock("10.0.144.0/20")
            .mapPublicIpOnLaunch(true)
            .availabilityZone(
                Output.all(availabilityZonesOutputs)
                    .applyValue {it ->
                        it.last().names().get(1)
                    }
            )
            .tags(mapOf("Owner" to "cesare.urso@r3.com", "Name" to "vodafone-eks-public2-subnet"))
            .build(),
        CustomResourceOptions.builder().parent(vpc).build()
    )
    ctx.export("prodSubnetPublic2Id", prodSubnetPublic2.id())

    val prodSubnetPublic3 = Subnet(
        "prodSubnetPublic3", SubnetArgs.builder()
            .vpcId(vpc.id())
            .cidrBlock("10.0.144.0/20")
            .mapPublicIpOnLaunch(true)
            .availabilityZone(
                Output.all(availabilityZonesOutputs)
                    .applyValue {it ->
                        it.last().names().get(2)
                    }
            )
            .tags(mapOf("Owner" to "cesare.urso@r3.com", "Name" to "vodafone-eks-public3-subnet"))
            .build(),
        CustomResourceOptions.builder().parent(vpc).build()
    )
    ctx.export("prodSubnetPublic3Id", prodSubnetPublic3.id())

    val prodSubnetPrivate1 = Subnet(
        "vodafoneSubnetPrivate1", SubnetArgs.builder()
            .vpcId(vpc.id())
            .cidrBlock("10.0.0.0/19")
            .mapPublicIpOnLaunch(false)
            .availabilityZone(
                Output.all(availabilityZonesOutputs)
                    .applyValue {it ->
                        it.last().names().get(0)
                    }
            )
            .tags(mapOf("Owner" to "cesare.urso@r3.com", "Name" to "vodafone-eks-private1-subnet"))
            .build(),
        CustomResourceOptions.builder().parent(vpc).build()
    )
    ctx.export("prodSubnetPrivate1Id", prodSubnetPrivate1.id())

    val prodSubnetPrivate2 = Subnet(
        "vodafoneSubnetPrivate2", SubnetArgs.builder()
            .vpcId(vpc.id())
            .cidrBlock("10.0.32.0/19")
            .mapPublicIpOnLaunch(false)
            .availabilityZone(
                Output.all(availabilityZonesOutputs).applyValue { it ->
                        it.last().names().get(1) }
            )
            .tags(mapOf("Owner" to "cesare.urso@r3.com", "Name" to "vodafone-eks-private2-subnet"))
            .build(),
        CustomResourceOptions.builder().parent(vpc).build()
    )
    ctx.export("prodSubnetPrivate2Id", prodSubnetPrivate2.id())

    val prodSubnetPrivate3 = Subnet(
        "prodSubnetPrivate3", SubnetArgs.builder()
            .vpcId(vpc.id())
            .cidrBlock("10.0.32.0/19")
            .mapPublicIpOnLaunch(false)
            .availabilityZone(
                Output.all(availabilityZonesOutputs).applyValue { it ->
                    it.last().names().get(2) }
            )
            .tags(mapOf("Owner" to "cesare.urso@r3.com", "Name" to "vodafone-rds-private3-subnet"))
            .build(),
        CustomResourceOptions.builder().parent(vpc).build()
    )
    ctx.export("prodSubnetPrivate3Id", prodSubnetPrivate3.id())


    //Create the Internet Gateway
    val prodIgw = InternetGateway(
        "vodafoneProdIgw", InternetGatewayArgs.builder()
            .vpcId(vpc.id())
            .tags(mapOf("Owner" to "cesare.urso@r3.com", "Name" to "vodafone-eks-vodafoneProdIgw"))
            .build(),
        CustomResourceOptions.builder().parent(vpc).build()
    )
    ctx.export("vodafoneProdIgwId", prodIgw.id())

    //Create the Routing Table for IG and internal for private subnets
    val prodPublicCrt = RouteTable(
        "vodafoneProdPublicCrt", RouteTableArgs.builder()
            .vpcId(vpc.id())
            .routes(
                RouteTableRouteArgs.builder()
                    .cidrBlock("0.0.0.0/0")
                    .gatewayId(prodIgw.id())
                    .build()
            )
            .tags(mapOf("Owner" to "cesare.urso@r3.com", "Name" to "vodafone-eks-prodPublicCrt"))
            .build(),
        CustomResourceOptions.builder()
            .dependsOn(listOf(vpc, prodIgw))
            .build()
    )

    val prodLocalCrt = RouteTable(
        "vodafoneProdLocalCrt", RouteTableArgs.builder()
            .vpcId(vpc.id())
            .tags(mapOf("Owner" to "cesare.urso@r3.com", "Name" to "vodafone-eks-prodPublicLocalCrt"))
            .build(),
        CustomResourceOptions.builder()
            .dependsOn(listOf( vpc))
            .build()
    )
    RouteTableAssociation("public-rta-zone1",
        RouteTableAssociationArgs.builder().routeTableId(prodPublicCrt.id()).subnetId(prodSubnetPublic1.id()).build()
    )
    RouteTableAssociation("public-rta-zone2",
        RouteTableAssociationArgs.builder().routeTableId(prodPublicCrt.id()).subnetId(prodSubnetPublic2.id()).build()
    )
    RouteTableAssociation("private-rta-zone1",
        RouteTableAssociationArgs.builder().routeTableId(prodLocalCrt.id()).subnetId(prodSubnetPrivate1.id()).build()
    )
    RouteTableAssociation("private-rta-zone2",
        RouteTableAssociationArgs.builder().routeTableId(prodLocalCrt.id()).subnetId(prodSubnetPrivate2.id()).build()
    )
    RouteTableAssociation("private-rta-zone3",
        RouteTableAssociationArgs.builder().routeTableId(prodSubnetPrivate2.id()).subnetId(prodSubnetPrivate3.id()).build()
    )
    RouteTableAssociation("private-rta-zone4",
        RouteTableAssociationArgs.builder().routeTableId(prodSubnetPrivate1.id()).subnetId(prodSubnetPrivate3.id()).build()
    )


    //Create Security Group
    val ec2AllowRule = SecurityGroup(
        "ec2AllowRule", SecurityGroupArgs.builder()
            .vpcId(vpc.id())
            .ingress(
                SecurityGroupIngressArgs.builder()
                    .description("ANY")
                    .fromPort(0)
                    .toPort(0)
                    .protocol("-1")
                    .cidrBlocks("0.0.0.0/0")
                    .build()
                    ,
                SecurityGroupIngressArgs.builder()
                    .protocol("tcp")
                    .fromPort(2225)
                    .toPort(2225)
                    .cidrBlocks("0.0.0.0/0").build()
                    ,
                SecurityGroupIngressArgs.builder()
                    .protocol("tcp")
                    .fromPort(2223)
                    .toPort(2223)
                    .cidrBlocks("0.0.0.0/0").build()
                    ,
                SecurityGroupIngressArgs.builder()
                    .description("HTTPS")
                    .fromPort(443)
                    .toPort(443)
                    .protocol("tcp")
                    .cidrBlocks("0.0.0.0/0")
                    .build()
                    ,
                SecurityGroupIngressArgs.builder()
                    .description("HTTP")
                    .fromPort(80)
                    .toPort(80)
                    .protocol("tcp")
                    .cidrBlocks("0.0.0.0/0")
                    .build(),
                SecurityGroupIngressArgs.builder()
                    .protocol("tcp")
                    .fromPort(5432)
                    .toPort(5432)
                    .cidrBlocks(listOf("0.0.0.0/0"))
                    .build(),
                SecurityGroupIngressArgs.builder()
                    .description("SSH")
                    .fromPort(22)
                    .toPort(22)
                    .protocol("tcp")
                    .cidrBlocks("0.0.0.0/0")
                    .build()
            )
            .egress(
                SecurityGroupEgressArgs.builder()
                    .fromPort(0)
                    .toPort(0)
                    .protocol("-1")
                    .cidrBlocks("0.0.0.0/0")
                    .build()
            )
            .tags(mapOf("Owner" to "cesare.urso@r3.com", "Name" to " Security Group that allows ssh,http,https"))
            .build()
    )

    // Create an IAM role for the EKS cluster
    val eksRole = Role(
            "eks-role",
            RoleArgs.builder()
                .assumeRolePolicy("{\"Version\": \"2012-10-17\",\"Statement\": [{\"Effect\": \"Allow\",\"Principal\": {\"Service\": \"eks.amazonaws.com\"},\"Action\": \"sts:AssumeRole\"}]}")
                .tags(mapOf("Owner" to "cesare.urso@r3.com", "Name" to "vodafone-eks-role"))
                .build()
        )
        ctx.export("eksRole", eksRole.name())

    // Attach the AmazonEKSClusterPolicy to the created role
    val eksClusterPolicyAttachment = RolePolicyAttachment(
            "eks-cluster-policy-attachment",
            RolePolicyAttachmentArgs.builder()
                .role(eksRole.name())
                .policyArn("arn:aws:iam::aws:policy/AmazonEKSClusterPolicy")
                .build()
        )
        ctx.export("eksClusterPolicyAttachment", eksClusterPolicyAttachment.id())

    // Attach the AmazonEKSServicePolicy to the created role
    val eksServicePolicyAttachment = RolePolicyAttachment(
            "eks-service-policy-attachment",
            RolePolicyAttachmentArgs.builder()
                .role(eksRole.name())
                .policyArn("arn:aws:iam::aws:policy/AmazonEKSServicePolicy")
                .build()
        )

    // Create an EKS cluster attached to the created VPC and Subnet
    val eksCluster = Cluster(
            "eks-cluster",
            ClusterArgs.builder()
                .roleArn(eksRole.arn())
                .vpcConfig(
                    ClusterVpcConfigArgs.builder()

                        .subnetIds(
                            Output.all(prodSubnetPrivate1.id(),prodSubnetPrivate2.id())
                        )
                        .build()
                )
            .tags(mapOf("Owner" to "cesare.urso@r3.com", "Name" to "vodafone-eks-cluster"))
            .build()
            ,
            CustomResourceOptions.builder()
                .dependsOn(listOf(prodSubnetPrivate1, prodSubnetPrivate2, vpc, eksRole, eksClusterPolicyAttachment, eksServicePolicyAttachment))
                .build()
        )
        ctx.export("eksCluster.clusterId", eksCluster.clusterId())
        ctx.export("eksClusterName", eksCluster.name())

        // Define subnet IDs for the CloudHSM cluster
    val cloudHsmSubnetIds = Output.all(prodSubnetPrivate1.id(),prodSubnetPrivate2.id()).applyValue { ids -> listOf(ids) }
        ctx.export("cloudHsmSubnetIds", cloudHsmSubnetIds)

        // Create a CloudHSM v2 cluster in the specified subnet
    val cloudHsmCluster = HsmCluster(
            "cloudhsm-cluster", HsmClusterArgs.builder()
                    .hsmType("hsm1.medium")
                    .subnetIds(
                        Output.all(prodSubnetPrivate1.id(),prodSubnetPrivate2.id())
                    )
                    .tags(mapOf("Owner" to "cesare.urso@r3.com", "Name" to "vodafone-cloudhsm-cluster"))
                .build(),
                CustomResourceOptions.builder()
                    .dependsOn(listOf(prodSubnetPrivate1, prodSubnetPrivate2, vpc))
                .build()
            )
           ctx.export("cloudHsmClusterId", cloudHsmCluster.clusterId())

//    // Create the First HSM instance (Hardware Security Module)
//    val hsm1 = Hsm(
//        "vodafone-hsm1", HsmArgs.builder()
//            .clusterId(cloudHsmCluster.id())
//            .subnetId(prodSubnetPrivate1.id())
//            .build(),
//            CustomResourceOptions.builder()
//            .dependsOn(listOf(prodSubnetPrivate1,  vpc))
//            .build()
//        )
//        ctx.export("hsm1Id", hsm1.id())
//        ctx.export("hsm1IpAddress", hsm1.ipAddress())

    // Create the second HMS
    val hsm2 = Hsm(
        "vodafone-hsm2", HsmArgs.builder()
            .clusterId(cloudHsmCluster.id())
            .subnetId(prodSubnetPrivate2.id())
            .build(),
        CustomResourceOptions.builder()
            .dependsOn(listOf(prodSubnetPrivate2, vpc))
            .build()
    )
    ctx.export("hsm2Id", hsm2.id())
    ctx.export("hsm2IpAddress", hsm2.ipAddress())

//    Cloud init happen with the cloud-hsm-init project
//    Create a local machine where to run the commands needed for the Cloud HSM initialisation
    val awsLinuxAmi = Ec2Functions.getAmi(
        GetAmiArgs.builder()
            .owners("amazon")
            .filters(
                GetAmiFilterArgs.builder()
                    .name("name")
                    .values("amzn2-ami-hvm-*-x86_64-ebs")
                    .build()
            )
        .mostRecent(true)
        .tags(mapOf("Owner" to "cesare.urso@r3.com", "Name" to "vodafone-awsLinuxAmi"))
        .build()
        ).applyValue{result -> result.id()}
    val securityGroupJumpServer = SecurityGroup(
        "vodafone-jump-server-secgrp", SecurityGroupArgs.builder()
            .description("jump server for CloudHSM activation")
            .vpcId(vpc.id())
            .ingress(
                listOf(
                    SecurityGroupIngressArgs.builder()
                    .protocol("tcp")
                    .fromPort(22)
                    .toPort(22)
                    .cidrBlocks("0.0.0.0/0").build()
                    ,
                    SecurityGroupIngressArgs.builder()
                        .protocol("tcp")
                        .fromPort(2225)
                        .toPort(2225)
                        .cidrBlocks("0.0.0.0/0").build()
                    ,
                    SecurityGroupIngressArgs.builder()
                        .protocol("tcp")
                        .fromPort(2223)
                        .toPort(2223)
                        .cidrBlocks("0.0.0.0/0").build()
                    ,
                    SecurityGroupIngressArgs.builder()
                        .description("ANY")
                        .fromPort(0)
                        .toPort(0)
                        .protocol("-1")
                        .cidrBlocks("0.0.0.0/0")
                        .build()
                    ,
                    SecurityGroupIngressArgs.builder()
                        .description("HTTPS")
                        .fromPort(443)
                        .toPort(443)
                        .protocol("tcp")
                        .cidrBlocks("0.0.0.0/0")
                        .build()
                    ,
                    SecurityGroupIngressArgs.builder()
                        .description("HTTP")
                        .fromPort(80)
                        .toPort(80)
                        .protocol("tcp")
                        .cidrBlocks("0.0.0.0/0")
                        .build())
            ).egress(
                listOf(
                    SecurityGroupEgressArgs.builder()
                    .protocol("-1")
                    .fromPort(0)
                    .toPort(0)
                    .cidrBlocks("0.0.0.0/0")
                    .build())
                )
            .build()
    )
    val keyPair = KeyPair("ec2-keypair", KeyPairArgs.builder()
        .keyName("jump-server-key-pair")
        .publicKey("ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABgQDKgqvp258z+UBTaq77A04wgg/ipL5nvj2lc421SQVLvzoLeZHo0rnFlRrrIIZ7wog7Qe7AjThygbmVHw5gHm+L8XjLeVlsxRS77NjkqWylkanAAbVO1hiZpTpT8bcDLOxTWQWnL5A2JP3r8ZgW4zzNNrTjA8GeRDaQph3Y3NBREDsq4WBghykK8NRHxeEDAJfDcVa3TuWQE+23quZKZXfgcyzgRY3icHZiPzDI0Wmr0QRaXSr9xAGU1EgmP/vV7QSr8hRWRHv9nNWwAJGjRueBM0XZVveukiAGlX1EvH00rL+76CuX4RhM+3UHNgWmfEv8O8c3ZZ++MqsY9UhrNbszMRVWMe9WcV4hryZ+xLLT/20Xl39VoxWGXtQWFebAMUsSeVKG4ZM+c0lMJPLFtxxkiAinGONtY3T1kEzOTwcOgtjAIGwRLe4R5OyjBiyj0XNNSOiRI0YNpkTbOfScDqlVDRmCwBSvGmI2RzwH42QU2AwP7i14sgFzrLVZ/HKpIxM= cesare.urso@LDNM-RC27HGD6XV")
        .build())
    ctx.export("keyPairName", keyPair.keyName())

    val jumpServer = Instance(
        "vodafone-jump-server", InstanceArgs.builder()
            .vpcSecurityGroupIds(Output.all(securityGroupJumpServer.id()))
            .tags(mapOf("Owner" to "cesare.urso@r3.com",  "Name" to "vodafone-jump-server"))
            .instanceType(Output.ofRight(InstanceType.T2_Micro))
            //.securityGroups(Output.all(securityGroupJumpServer.id()))
            .subnetId(prodSubnetPublic2.id())
            .availabilityZone(
                Output.all(availabilityZonesOutputs).applyValue { it ->
                    it.last().names().get(1) }
            )
            .associatePublicIpAddress(true)
            .keyName(keyPair.keyName())
            .ami(awsLinuxAmi)
//            .userData(
//                        """
//                            #!/bin/bash
//                            curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
//                            unzip awscliv2.zip
//                            sudo ./aws/install
//                            aws --version
//                         """
//            )
            .tags(mapOf("Owner" to "cesare.urso@r3.com", "Name" to "vodafone-jump-server"))
            .build(),
            CustomResourceOptions.builder()
                .dependsOn(listOf( vpc, keyPair))
            .build()
    )
    ctx.export("jumpServerPublicIp", jumpServer.publicIp())
    ctx.export("jumpServerId", jumpServer.id())
    ctx.export("jumpServerPublicDns", jumpServer.publicDns())

//    val dbSubnetGroup = SubnetGroup("dbSubnetGroup", SubnetGroupArgs.builder()
//        .subnetIds(
//            Output.all(prodSubnetPrivate3.id())
//        )
//        .build())
//    ctx.export("dbSubnetGroupName", dbSubnetGroup.name())

    val dbInstance = RdsInstance(
        "postgres-db-instance",
        RdsInstanceArgs
            .builder()
            .dbSubnetGroupName(
                String.format("[^a-z0-9-_]", prodSubnetPrivate3.id())
            )
            .instanceClass("db.t2.micro")
            .engine("postgres")
            .engineVersion("13.2")
            .dbName("postgresdb")
            .username("postgres")
            .password("mySecurePassword")
            .vpcSecurityGroupIds( ec2AllowRule.id().applyValue { listOf( it) })
            .multiAz(true)
            .storageType("gp2")
            .skipFinalSnapshot(true)
            .allocatedStorage(20)
            .tags(mapOf("Owner" to "cesare.urso@r3.com", "Name" to "vodafone-jump-server"))
            .build()
        ,
        CustomResourceOptions.builder().parent(prodSubnetPrivate3).build()
    )

//   //Create the KMS for Cloud HSM for local AWS account PS 450694575897 (to be replaced for dev  vs Client deployment) or Vodafone 331173408599
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
////        "AWS": "arn:aws:iam::331173408599:root"
////      },
////      "Action": "kms:*",
////      "Resource": "*"
////    },
////    {
////                                "Sid": "Allow administration of the key",
////                                "Effect": "Allow",
////                                "Principal": {
////                                    "AWS": [
////                                        "arn:aws:iam::331173408599:role/admin"
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






