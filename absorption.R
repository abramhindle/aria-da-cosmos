library(zoo)



datfiles <- list.files(path = "./stars/",pattern="*.dat")
datfiles = datfiles[datfiles!="irstphot.dat"]
datfiles = datfiles[datfiles!="lew.dat"]
datfiles = datfiles[datfiles!="synphot.dat"]
filter <- function(u) {
    u[u$V1 >= 3500 & u$V1 <= 7500,]
}

saferoll <- function(fv,k=10) {
    out <- rollmean(fv,k=k,na.pad=TRUE)
    out[is.na(out)] <- fv[is.na(out)]
    out
}

process <- function(file) {
    v <- read.table(paste("stars/",file,sep=""))
    filt <- filter(v)
    filt$fv <- filt$V2
    filt$u <- (fv - saferoll(fv))**2
    filt$line = u>mean(u)
    out <- c()
    out$angstrom <- filt$V1
    out$value <- filt$V2
    out$u <- filt$u
    out$line <- filt$line
    data.frame(out)
}

file <- datfiles[1]
vvv <- process(datfiles[1])
summary(vvv[vvv$line,])

# datfiles <- c("b0v.dat")

sapply(datfiles,function(file) {
    print(file)
    func <- function() {
        vvv <- process(file)
        write.csv(vvv[vvv$line,], paste("absorption",file,sep="/"))
    }
    func()
    #try(func(),FALSE)
})
