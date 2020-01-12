terraform {
  backend "local" {
    path = "tf_backend/scraper.tfstate"
  }
}

variable "AWS_ACCESS_KEY" {
}

variable "AWS_SECRET_ACCESS_KEY" {
}

data "aws_iam_role" "role" {
  name = "apis-for-all-service-account"
}

provider "aws" {
  region     = "us-east-1"
  access_key = var.AWS_ACCESS_KEY
  secret_key = var.AWS_SECRET_ACCESS_KEY
}

resource "aws_appautoscaling_target" "dynamodb_table_read_target" {
  max_capacity       = 10
  min_capacity       = 1
  resource_id        = "table/stock-symbols"
  role_arn           = data.aws_iam_role.role.arn
  scalable_dimension = "dynamodb:table:ReadCapacityUnits"
  service_namespace  = "dynamodb"
  depends_on		 = [aws_dynamodb_table.stock-symbols]
}

resource "aws_appautoscaling_target" "dynamodb_table_write_target" {
  max_capacity       = 10
  min_capacity       = 1
  resource_id        = "table/stock-symbols"
  role_arn           = data.aws_iam_role.role.arn
  scalable_dimension = "dynamodb:table:WriteCapacityUnits"
  service_namespace  = "dynamodb"
  depends_on		 = [aws_dynamodb_table.stock-symbols, aws_appautoscaling_target.dynamodb_table_read_target]
}


resource "aws_dynamodb_table" "stock-symbols" {
  name           = "stock-symbols"
  read_capacity  = 1
  write_capacity = 1
  hash_key       = "symbol"
  range_key      = "date"

  attribute {
    name = "symbol"
    type = "S"
  }

  attribute {
    name = "date"
    type = "S"
  }

  lifecycle {
    ignore_changes = [
      read_capacity,
      write_capacity,
    ]
  }
}

